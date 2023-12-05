const { createApp } = Vue;

createApp({
    data() {
        return {
            cardNumber: "",
            cvv: "",
            checkoutSubtotal: 0,
            wines: [],
            accessories: [],
            deliveryAddress: "",
            cart: [],
            pdfURL: null,
        };
    },

    created() {
        this.cart = JSON.parse(localStorage.getItem("cart")) || [];
    },

    methods: {
        createPurchase() {
            this.wines = this.cart.filter((product) =>
                product.hasOwnProperty("area")
            );
            this.accessories = this.cart.filter(
                (product) => !product.hasOwnProperty("area")
            );

            let paymentInfo = {
                numberCard: this.cardNumber,
                cvvCard: this.cvv,
                description: "Wine Lovers Society payment",
                amount: this.checkoutSubtotal,
            };
            let purchaseInfo = {
                deliveryAddress: this.deliveryAddress,
                wines: this.wines.map((wine) => {
                    return {
                        productId: wine.id,
                        amount: wine.amount,
                    };
                }),
                accessories: this.accessories.map((accessory) => {
                    return {
                        productId: accessory.id,
                        amount: accessory.amount,
                    };
                }),
            };

            let purchaseDTO = {
                newPurchaseApp: purchaseInfo,
                payWithCardApp: paymentInfo,
            };

            axios({
                method: "post",
                url: "/api/purchase",
                data: purchaseDTO,
                responseType: "blob",
            })
                .then((response) => {
                    //  let blob = new Blob([response.data], { type: "application/pdf" });
                    //   this.pdfURL = URL.createObjectURL(blob);
                    //   console.log(blob);

                    let link = document.createElement("a");
                    link.href = URL.createObjectURL(
                        new Blob([response.data], { type: "application/pdf" })
                    );
                    link.setAttribute("download", "account-details.pdf");
                    link.click();

                    Swal.fire({
                        icon: "success",
                        title: "<span style='color: #000;'>Done!</span>",
                        text: "Order received",
                        customClass: {
                            popup: "text-center",
                        },
                        color: "#000",
                        background: "#fff",
                        confirmButtonColor: "#880000",
                    });
                    this.cart = [];
                    this.saveLocalStorage();
                    setTimeout(() => location.pathname = "/web/pages/catalogue.html", 2000);
                })
                .catch((error) => {
                  console.log(error);
                });
        },

        messageError(message) {
            Swal.fire({
                icon: "error",
                title: "<span style='color: #000;'>An error has occurred</span>",
                text: message,
                customClass: {
                    popup: "text-center",
                },
                titleColor: "#000",
                color: "#000",
                background: "#fff",
                confirmButtonColor: "#880000",
            });
        },

        addMoreWinesToCart(wine) {
            wine.amount++;
            wine.subTotal = wine.price * wine.amount;
            this.cart.push();
            this.saveLocalStorage();
        },
        substractOneWine(wine) {
            wine.amount--;
            wine.subTotal = wine.price * wine.amount;
            if (wine.amount === 0) {
                this.cart = this.cart.filter((wines) => wines.amount >= 1);
            }
            this.cart.push();
            this.saveLocalStorage();
        },
        deleteWineFromCart(wine) {
            wine.amount = 0;
            wine.subTotal = 0;
            this.cart = this.cart.filter((wines) => wines.id !== wine.id);
            this.saveLocalStorage();
        },
        saveLocalStorage() {
            localStorage.setItem("cart", JSON.stringify(this.cart));
        },
    },
    computed: {
        updateSubtotal() {
            this.checkoutSubtotal = this.cart.reduce(
                (accumulatedTotal, product) =>
                    accumulatedTotal + product.subTotal,
                0
            );
        },
    },
}).mount("#app");
