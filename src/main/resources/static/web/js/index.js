const { createApp } = Vue;

createApp({
    data() {
        return {
            email: "",
            password: "",
            showLogin: true,
            errorEmail: false,
            registerBirthDate: "",
            registerName: "",
            registerLastName: "",
            registerPass: "",
            registerEmail: "",
            cart: [],
            priceOfTheCart: 0,
        };
    },

    created() {
        $(document).ready(function () {
            $(".counter").each(function () {
                $(this)
                    .prop("Counter", 0)
                    .animate(
                        {
                            Counter: $(this).text(),
                        },
                        {
                            duration: 4000,
                            easing: "swing",
                            step: function (now) {
                                $(this).text(Math.ceil(now));
                            },
                        }
                    );
            });
        });
        this.cart = JSON.parse(localStorage.getItem("cart")) || [];
    },

    methods: {
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

        saveLocalStorage() {
            localStorage.setItem("cart", JSON.stringify(this.cart));
        },

        catalogue() {
            location.pathname = "/web/pages/catalogue.html";
        },

        login() {
            let infoLogin = `email=${this.email}&password=${this.password}`;

            axios
                .post("/api/login", infoLogin)
                .then((response) => {
                    console.log("Successful request", response.data);

                    Swal.fire({
                        position: "center",
                        icon: "success",
                        title: "Welcome",
                        showConfirmButton: false,
                    });
                    setTimeout(() => {
                        location.pathname = "/web/pages/catalogue.html";
                    }, 1500);
                })
                .catch((err) => {
                    console.log(err);
                    if ((this.email = "" || this.password === "")) {
                        Swal.fire({
                            icon: "error",
                            title: "Error...",
                            text: "Please complete all information",
                            showConfirmButton: false,
                        });
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Invalid user",
                            text: "User or password is invalid",
                            showConfirmButton: false,
                            timer: 2000,
                        });
                    }
                })
                .finally(() => {
                    this.email = "";
                    this.password = "";
                });
        },

        logOut() {
            axios
                .post("/api/logout")
                .then((response) => {
                    console.log("signed out!!!");
                    Swal.fire({
                        position: "center",
                        icon: "warning",
                        title: "Your session has been closed",
                        showConfirmButton: false
                    });
                    setTimeout(() => {
                        window.location.href = "/index.html";
                    }, 3000);
                })
                .catch((err) => console.log("error"));
        },

        register() {
            let registerInfo = {
                firstName: this.registerName,
                lastName: this.registerLastName,
                email: this.registerEmail,
                password: this.registerPass,
                birthDate: this.registerBirthDate,
            };
            console.log(registerInfo);

            axios
                .post("/api/clients", registerInfo)

                .then((response) => {
                    console.log("registered");
                    let infoLogin = `email=${this.registerEmail}&password=${this.registerPass}`;

                    axios.post("/api/login", infoLogin).then((response) => {
                        location.pathname = "/web/pages/catalogue.html";
                    });
                })
                .catch((err) => {
                    console.log(err);
                    this.messageError(err.response.data);
                })
                .finally(() => {
                    this.email = "";
                    this.password = "";
                    this.name = "";
                    this.lastName = "";
                    this.birthDate = "";
                });
        },

        isLoggedIn() {
            return document.cookie.includes("JSESSIONID");
        },

        goToCheckout() {
            if (this.isLoggedIn()) {
                location.pathname = "/web/pages/checkout.html";
            } else {
                $("#exampleModal").modal("show");
                $("#offcanvasScrolling").offcanvas("hide");
            }
        },
    },
    computed: {
        calculatePriceOfTheCart() {
            this.priceOfTheCart = this.cart.reduce(
                (accumulatedTotal, product) =>
                    accumulatedTotal + product.subTotal,
                0
            );
        },
    },
}).mount("#app");
