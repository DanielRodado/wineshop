const { createApp } = Vue;

createApp({
    data() {
        return {
          orders: [],
          clientIsAdmin: false,
        };
    },

    created() {
        this.getCurrentClient();
        this.getOrders()
    },

    methods: {
        getOrderDetails(id) {
            axios({
                method: "post",
                url: `/api/purchase/order/receipt?purchaseId=${id}`,
                responseType: "blob",
            })
                .then((response) => {

                    let link = document.createElement("a");
                    link.href = URL.createObjectURL(
                        new Blob([response.data], { type: "application/pdf" })
                    );
                    link.setAttribute("download", "account-details.pdf");
                    link.click();

                    Swal.fire({
                        icon: "success",
                        title: "<span style='color: #000;'>Done!</span>",
                        text: "Order receipt downloaded",
                        customClass: {
                            popup: "text-center",
                        },
                        color: "#000",
                        background: "#fff",
                        confirmButtonColor: "#880000",
                    });
                })
                .catch((error) => {
                  console.log(error);
                });
        },

        getOrders(){
            axios
            .get('/api/purchase/client')
            .then((response) => {
                this.orders = response.data
                console.log(this.orders)
            })
            .catch((error)=>{console.log('Error al obtener las compras del cliente', error)});
        },

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
                    this.clientIsAdmin = false
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
                    setTimeout(() => {
                        location.pathname = "/index.html";
                    }, 1875);
                })
                .catch((err) => console.log("error"));
        },
    },
    computed: {
        
    },
}).mount("#app");
