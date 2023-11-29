const { createApp } = Vue;

createApp({
    data() {
        return {
            wines: [],
            wineVarietiesList: [],
            vineyards: new Set(),
            areas: new Set(),
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

        axios
            .get("/api/wines/varieties")
            .then((response) => {
                this.wineVarietiesList = response.data;
            })
            .catch((error) => {
                console.log("error");
                console.log(error);
            });

        axios
            .get("/api/wines")
            .then((response) => {
                this.wines = response.data;
                console.log(this.wines);
                for (let wine of this.wines) {
                    this.vineyards.add(wine.vineyard);
                }

                for (let wine of this.wines) {
                    this.areas.add(wine.area.trim());
                }

                console.log(this.vineyards);
                console.log(this.areas);
            })
            .catch((error) => {
                console.log("error");
                console.log(error);
            });

        axios
            .get("/api/accessories")
            .then((response) => {
                this.accessories = response.data;
            })
            .catch((error) => {
                console.log("error");
                console.log(error);
            });
    },

    methods: {
        catalogue() {
            location.href = "/web/pages/catalogue.html";
        },

        errorMessage(message) {
            Swal.fire({
                icon: "error",
                title: "An error has occurred",
                text: message,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
            });
        },
    },
}).mount("#app");
