package com.wineshop.ecommerce.controllers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wineshop.ecommerce.dto.*;
import com.wineshop.ecommerce.models.*;
import com.wineshop.ecommerce.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;


import static com.wineshop.ecommerce.utils.ProductUtil.calculatePriceOrder;
import static com.wineshop.ecommerce.utils.PurchaseUtil.deletePurchaseAndProductPurchase;
import static java.awt.Color.BLACK;

@RestController
@RequestMapping("/api")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private WinePurchaseService winePurchaseService;

    @Autowired
    private AccessoryPurchaseService accessoryPurchaseService;

    @Autowired
    private WineService wineService;

    @Autowired
    private AccessoryService accessoryService;

    @Autowired
    private ClientService clientService;

    public ResponseEntity<Object> createPurchase(NewPurchaseApplicationDTO newPurchaseApp) {

            // si las dos listas están vacias, manda un error
            if (newPurchaseApp.getWines().isEmpty() && newPurchaseApp.getAccessories().isEmpty()) {
                return new ResponseEntity<>("Order cannot be empty", HttpStatus.FORBIDDEN);
            }

            if (newPurchaseApp.getDeliveryAddress().isBlank()) {
                return new ResponseEntity<>("The delivery address is missing", HttpStatus.FORBIDDEN);
            }

            Purchase purchase = new Purchase(newPurchaseApp.getDeliveryAddress(), LocalDate.now());
            Long purchaseId = purchaseService.savePurchaseAndGetId(purchase);

            double priceTotalOrder = 0.0;
            if (!newPurchaseApp.getWines().isEmpty()) {

                for (ProductRecieverDTO wine: newPurchaseApp.getWines()) {
                    // El ID de los 'Wines' exista -> Services;
                    if (!wineService.existsWineById(wine.getProductId())) {
                        return new ResponseEntity<>("One of the wines you are trying to buy doesn't exist.",
                                HttpStatus.FORBIDDEN);
                    }
                    // La cantidad de Wine a comprar no sea menor o igual a 0;
                    if (wine.getAmount() <= 0) {
                        return new ResponseEntity<>("The amount of wines that you are trying to buy cannot be 0", HttpStatus.FORBIDDEN);
                    }

                    // Comprobar si la cantidad a comprar no supere el stock del 'Wine';
                    if (!wineService.existsWineByIdAndStockGreaterThan(wine.getProductId(), wine.getAmount() - 1)) {
                        return new ResponseEntity<>("Right now, we don't have wines in stock to supply your order.",
                                HttpStatus.FORBIDDEN);
                    }

                    WinePurchase winePurchase = new WinePurchase(
                            wine.getAmount(), calculatePriceOrder(
                            wine.getAmount(), wineService.getPriceWineById(wine.getProductId())));

                    winePurchase.setWine(wineService.findWineById(wine.getProductId()));

                    priceTotalOrder += calculatePriceOrder(
                            wine.getAmount(), wineService.getPriceWineById(wine.getProductId()));

                    // Modificar el stock del 'Wine' -> Hacer las querys en el repositorio.
                    wineService.updateStockWineById(wine.getProductId(), wine.getAmount());
                    purchase.addWinePurchase(winePurchase);
                    winePurchaseService.saveWinePurchase(winePurchase);
                }
            }


            if (!newPurchaseApp.getAccessories().isEmpty()) {

                for (ProductRecieverDTO accessory : newPurchaseApp.getAccessories()) {
                    // Comprobar que el ID de los 'Wines' exista -> Services;
                    if (!accessoryService.existsAccessoryById(accessory.getProductId())) {
                        return new ResponseEntity<>("One of the accessories you are trying to buy doesn't exist.",
                                HttpStatus.FORBIDDEN);
                    }

                    // Comprobar que la cantidad de Wine a comprar no sea menor o igual a 0;
                    if (accessory.getAmount() <= 0) {
                        return new ResponseEntity<>("The amount of accessories that you are trying to buy cannot be 0",
                                HttpStatus.FORBIDDEN);
                    }

                    // Comprobar si la cantidad a comprar no supere el stock del 'Wine';
                    if (!accessoryService.existsAccessoryByIdAndStockGreaterThan(accessory.getProductId(),
                            accessory.getAmount()-1)) {
                        return new ResponseEntity<>("Right now, we don't have accessories in stock to supply your order.",
                                HttpStatus.FORBIDDEN);
                    }

                    AccessoryPurchase accessoryPurchase = new AccessoryPurchase(
                            accessory.getAmount(), calculatePriceOrder(
                            accessory.getAmount(), accessoryService.getPriceAccessoryById(accessory.getProductId())));

                    accessoryPurchase.setAccessory(accessoryService.findAccessoryById(accessory.getProductId()));

                    priceTotalOrder += calculatePriceOrder(
                            accessory.getAmount(), accessoryService.getPriceAccessoryById(accessory.getProductId()));

                    // Modificar el stock del 'Accessory' -> Hacer las querys en el repositorio.
                    accessoryService.updateStockAccessoryById(accessory.getProductId(), accessory.getAmount());

                    purchase.addAccessoryPurchases(accessoryPurchase);
                    accessoryPurchaseService.saveAccessoryPurchase(accessoryPurchase);
                }
            }

            purchase.setPriceOrder(priceTotalOrder);
            Client client = clientService.findClientById(46L);
            client.addPurchase(purchase);
            purchaseService.savePurchase(purchase);


            return new ResponseEntity<>(purchaseId, HttpStatus.CREATED);

    }

    public ResponseEntity<Object> payWithCard(PayWithCardApplicationDTO payWithCardApp) {

        // Aquí se crea una instancia de WebClient, que es la clase principal para hacer solicitudes
        // HTTP en Spring WebFlux. Se configura con la URL base de la API a la que vamos a realizar solicitudes.
        WebClient webClient = WebClient.create("https://finovate-bank.onrender.com");

        // Se realiza la solicitud POST con el objeto en el cuerpo
        ClientResponse response = webClient.post()
                .uri("/api/cards/pay")
                .body(BodyInserters.fromValue(payWithCardApp))
                .exchange()
                .block();

        try {
            assert response != null;
        } catch(Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(
                response.bodyToMono(String.class).block(), response.statusCode().value() == 200 ? HttpStatus.OK :
                HttpStatus.FORBIDDEN);
    }

    @PostMapping("/purchase")
    @Transactional
    public ResponseEntity<Object> payAndCreatePurchase(@RequestBody PurchasePaymentWithCardApplicationDTO purchasePaymentWithCardApp)
    throws Exception {

        ResponseEntity<Object> purchaseResponse = createPurchase(purchasePaymentWithCardApp.getNewPurchaseApp());

        if (purchaseResponse.getStatusCode().value() != 201) {
            return purchaseResponse;
        }

        Double priceOrderPurchase = purchaseService.getPriceOrderOfPurchaseById((Long) purchaseResponse.getBody());

        Purchase purchase = purchaseService.getPurchaseById((Long) purchaseResponse.getBody());

        if (!Objects.equals(priceOrderPurchase, purchasePaymentWithCardApp.getPayWithCardApp().getAmount())) {

            deletePurchaseAndProductPurchase(winePurchaseService, accessoryPurchaseService, purchaseService,
                    purchasePaymentWithCardApp.getNewPurchaseApp().getWines(),
                    purchasePaymentWithCardApp.getNewPurchaseApp().getAccessories(), purchase);

            return new ResponseEntity<>("Payment amount doesn't match the order's price", HttpStatus.FORBIDDEN);
        }

        // crear pdf
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Order receipt.pdf"));

        document.open();
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BLACK);
        Font notBoldFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BLACK);

        // agregamos logo
        PdfPTable logo = new PdfPTable(1);
        logo.setWidthPercentage(100);

        Image img = Image.getInstance(Objects.requireNonNull(getClass().getResource("/static/web/images/main-logo.png")));

        img.scaleToFit(300, 120);
        img.setAlignment(Image.ALIGN_BASELINE);
        PdfPCell imageCell = new PdfPCell(img);
        imageCell.setBorder(PdfPCell.NO_BORDER);
        logo.addCell(imageCell);

        document.add(logo);

        // tabla para los vinos
        if ( !purchasePaymentWithCardApp.getNewPurchaseApp().getWines().isEmpty() ) {
            // agregamos tabla
            PdfPTable tableTitle = new PdfPTable(1);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPaddingBottom(20);
            cell.addElement(new Paragraph("Your wines:", boldFont));
            tableTitle.addCell(cell);
            document.add(tableTitle);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100); // Asegurar que la tabla ocupe el ancho completo

            PdfPCell headerCell1 = new PdfPCell(new Phrase("Wine name", boldFont));
            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell1.setPadding(10);
            table.addCell(headerCell1);

            PdfPCell headerCell2 = new PdfPCell(new Phrase("Amount", boldFont));
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setPadding(10);
            table.addCell(headerCell2);

            PdfPCell headerCell3 = new PdfPCell(new Phrase("Price", boldFont));
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setPadding(10);
            table.addCell(headerCell3);

            Set<ProductRecieverDTO> wines = purchasePaymentWithCardApp.getNewPurchaseApp().getWines();

            for (ProductRecieverDTO wine : wines) {
                PdfPCell wineName = new PdfPCell(new Phrase(wineService.getWineNameById(wine.getProductId()), notBoldFont));
                wineName.setHorizontalAlignment(Element.ALIGN_CENTER);
                wineName.setPadding(7);
                table.addCell(wineName);

                PdfPCell wineAmount = new PdfPCell(new Phrase("" +  wine.getAmount(), notBoldFont));
                wineAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                wineAmount.setPadding(7);
                table.addCell(wineAmount);

                PdfPCell winePrice = new PdfPCell(new Phrase("$" + calculatePriceOrder(wine.getAmount(), wineService.getPriceWineById(wine.getProductId())), notBoldFont));
                winePrice.setHorizontalAlignment(Element.ALIGN_CENTER);
                winePrice.setPadding(7);
                table.addCell(winePrice);
            }
            document.add(table);
        }

        // salto de linea
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        // tabla para los accesorios
        if ( !purchasePaymentWithCardApp.getNewPurchaseApp().getAccessories().isEmpty() ) {
            // agregamos tabla
            PdfPTable tableTitle = new PdfPTable(1);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPaddingBottom(20);
            cell.addElement(new Paragraph("Your accessories:", boldFont));
            tableTitle.addCell(cell);
            document.add(tableTitle);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100); // Asegurar que la tabla ocupe el ancho completo

            PdfPCell headerCell1 = new PdfPCell(new Phrase("Accessory name", boldFont));
            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell1.setPadding(10);
            table.addCell(headerCell1);

            PdfPCell headerCell2 = new PdfPCell(new Phrase("Amount", boldFont));
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setPadding(10);
            table.addCell(headerCell2);

            PdfPCell headerCell3 = new PdfPCell(new Phrase("Price", boldFont));
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setPadding(10);
            table.addCell(headerCell3);

            Set<ProductRecieverDTO> accessories = purchasePaymentWithCardApp.getNewPurchaseApp().getAccessories();

            for (ProductRecieverDTO accessory : accessories) {
                PdfPCell accessoryName = new PdfPCell(new Phrase(accessoryService.getAccessoryNameById(accessory.getProductId()), notBoldFont));
                accessoryName.setHorizontalAlignment(Element.ALIGN_CENTER);
                accessoryName.setPadding(7);
                table.addCell(accessoryName);

                PdfPCell accessoryAmount = new PdfPCell(new Phrase("" +  accessory.getAmount(), notBoldFont));
                accessoryAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                accessoryAmount.setPadding(7);
                table.addCell(accessoryAmount);

                PdfPCell accessoryPrice = new PdfPCell(new Phrase("$" + calculatePriceOrder(accessory.getAmount(), accessoryService.getPriceAccessoryById(accessory.getProductId())), notBoldFont));
                accessoryPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
                accessoryPrice.setPadding(7);
                table.addCell(accessoryPrice);

            }
            document.add(table);
        }

        document.add(new Paragraph("\n"));

        // id de la orden
        Paragraph orderId = new Paragraph("Order ID: ");
        Chunk boldOrderId = new Chunk("" + purchase.getId(), boldFont);
        orderId.add(boldOrderId);

        document.add(orderId);

        // salto de linea
        document.add(new Paragraph("\n"));

        // valor total de la compra

        Paragraph totalPrice = new Paragraph("Order total price: ");
        Chunk boldPriceNumber = new Chunk("$" + priceOrderPurchase, boldFont);
        totalPrice.add(boldPriceNumber);

        document.add(totalPrice);

        // salto de linea
        document.add(new Paragraph("\n"));

        // direccion de envio

        Paragraph deliveryAddress = new Paragraph("Delivery address: ");
        Chunk deliveryAddressBold = new Chunk(purchase.getDeliveryAddress(), boldFont);
        deliveryAddress.add(deliveryAddressBold);

        document.add(deliveryAddress);

        // cerramos el documento

        document.close();

        ResponseEntity<Object> paymentResponse = payWithCard(purchasePaymentWithCardApp.getPayWithCardApp());

        if (paymentResponse.getStatusCode().value() == 200) {

            // enviamos la respuesta en el body
            return new ResponseEntity<>(document, HttpStatus.OK);
        } else {
            deletePurchaseAndProductPurchase(winePurchaseService, accessoryPurchaseService, purchaseService,
                    purchasePaymentWithCardApp.getNewPurchaseApp().getWines(),
                    purchasePaymentWithCardApp.getNewPurchaseApp().getAccessories(), purchase);
            return paymentResponse;
        }
    }

    // servlet para cambiar el status
    @PatchMapping("/purchase/status")
    public ResponseEntity<Object> changePurchaseStatus(@RequestParam Long purchaseId,
                                                       @RequestParam String purchaseStatus){

        if (!purchaseService.existsPurchaseById(purchaseId)){
            return new ResponseEntity<>("This order does not exist", HttpStatus.FORBIDDEN);
        }

        if (Arrays.stream(PurchaseStatus.values()).anyMatch(status -> status.equals(purchaseStatus))) {
            return new ResponseEntity<>("The status doesn't match a valid option", HttpStatus.FORBIDDEN);
        }

        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        purchase.setStatus(PurchaseStatus.valueOf(purchaseStatus));
        purchaseService.savePurchase(purchase);

        return  new ResponseEntity<>("Order status updated", HttpStatus.OK);
    }

    // servlet para enviar el status
    // Authentication authentication
    // Client client = clientService.findClientByEmail(authentication.getName());
    //
    //        if (!purchaseService.existsPurchaseByIdAndClient(purchaseId, client)) {
    //            return new ResponseEntity<>("This order does not belong to you!", HttpStatus.FORBIDDEN);
    //        }
    @GetMapping("/purchase/status")
    public ResponseEntity<Object> getPurchaseStatus (@RequestParam Long purchaseId){

        if (!purchaseService.existsPurchaseById(purchaseId)){
            return new ResponseEntity<>("This order does not exist", HttpStatus.FORBIDDEN);
        }

        Purchase purchase = purchaseService.getPurchaseById(purchaseId);

        PurchaseStatus purchaseStatus = purchase.getStatus();

        return new ResponseEntity<>(purchaseStatus, HttpStatus.OK);
    }
}
