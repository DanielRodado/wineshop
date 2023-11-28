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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.time.LocalDate;
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
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BLACK);
        Font notBoldFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BLACK);

        // agregamos logo
        PdfPTable logo = new PdfPTable(1);
        logo.setWidthPercentage(100);

        Image img = Image.getInstance(Objects.requireNonNull(getClass().getResource("/static/web/images/main-logo.png")));

        img.scaleToFit(200, 56);
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
            table.addCell("Wine name");
            table.addCell("Amount");
            table.addCell("Price");

            Set<ProductRecieverDTO> wines = purchasePaymentWithCardApp.getNewPurchaseApp().getWines();

            for (ProductRecieverDTO wine : wines) {
                table.addCell(wineService.getWineNameById(wine.getProductId()));
                table.addCell("" +  wine.getAmount());
                table.addCell("$" + calculatePriceOrder(wine.getAmount(), wineService.getPriceWineById(wine.getProductId())));
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
            table.addCell("Accessory name");
            table.addCell("Amount");
            table.addCell("Price");

            Set<ProductRecieverDTO> accessories = purchasePaymentWithCardApp.getNewPurchaseApp().getWines();

            for (ProductRecieverDTO accessory : accessories) {
                table.addCell(accessoryService.getAccessoryNameById(accessory.getProductId()));
                table.addCell("" +  accessory.getAmount());
                Double accessoryPrice = accessoryService.getPriceAccessoryById(accessory.getProductId());
                table.addCell("$" + accessory.getAmount() * accessoryPrice);
            }
            document.add(table);
        }

        // salto de linea
        document.add(new Paragraph("\n"));

        // valor total de la compra

        Paragraph totalPrice = new Paragraph("Order total price: $" + priceOrderPurchase);

        document.add(totalPrice);

        // salto de linea
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        // direccion de envio

        Paragraph deliveryAddress = new Paragraph("Delivery address: " + purchase.getDeliveryAddress());

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

}
