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
           clientIsAdmin: false
           isAuthenticated: false
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
        this.isLoggedIn();
        this.getCurrentClient()
    },

    methods: {
    getCurrentClient() {
      axios
      .get("/api/clients/current")
      .then((response) => {
          this.clientIsAdmin = response.data.admin
      })
      .catch((error) => {
        console.log(error)
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
                    Swal.fire({
                        position: "center",
                        icon: "success",
                        title: "<span style='color: #000;'>Welcome!</span>",
                        color: "#000",
                        background: "#fff",
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
                            title: "<span style='color: #000;'>Please complete all information!</span>",
                            text: "Please complete all information",
                            showConfirmButton: false,
                            color: "#000",
                            background: "#fff",
                        });
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "<span style='color: #000;'>Error...</span>",
                            text: "User or password is invalid",
                            showConfirmButton: false,
                            color: "#000",
                            background: "#fff",
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
                .then(() => {
                    this.isLoggedIn();
                    Swal.fire({
                        icon: "success",
                        title: "<span style='color: #000;'>Your session has been closed!</span>",
                        customClass: {
                            popup: "text-center",
                        },
                        color: "#000",
                        background: "#fff",
                        confirmButtonColor: "#880000",
                    });
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

                .then(() => {
                    console.log("registered");
                    let infoLogin = `email=${this.registerEmail}&password=${this.registerPass}`;

                    axios.post("/api/login", infoLogin).then(() => {
                        Swal.fire({
                            icon: "success",
                            title: "<span style='color: #000;'>Welcome!</span>",
                            text: "Successful registration",
                            customClass: {
                                popup: "text-center",
                            },
                            color: "#000",
                            background: "#fff",
                            confirmButtonColor: "#880000",
                        });

                        setTimeout(
                            () =>
                                (location.pathname =
                                    "/web/pages/catalogue.html"),
                            1500
                        );
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
            axios("/api/clients/online")
                .then(() => {
                    this.isAuthenticated = true;
                })
                .catch(() => {
                    this.isAuthenticated = false;
                });
        },
        goToCheckout() {
            axios("/api/clients/online")
                .then((res) => {
                    location.pathname = "/web/pages/checkout.html";
                })
                .catch((err) => {
                    $("#exampleModal").modal("show");
                    $("#offcanvasScrolling").offcanvas("hide");
                });
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
