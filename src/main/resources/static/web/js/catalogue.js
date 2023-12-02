new Vue({
    el: "#app",
    data: {
        wines: [],
        winesFilter: [],
        loader: true,
        listOfPages: [],
        pageNumber: 1,
        pagesN: 1,
        wineDetail: {
            imgURL: ["a", "b"],
            stock: 2,
            price: 2000,
            variety: "A_A",
        },
        cart: [],
        priceOfTheCart: 0,
        vineyarsSelected: [],
        areaSelected: [],
        varietySelected: [],
        typeWine: "",
    },

    created() {
        this.getWines(); // catantidad a comprar, precio total
    },

    methods: {
        getWines() {
            axios
                .get("/api/wines")
                .then(({ data }) => {
                    this.wines = [...data];

                    for (let wine of this.wines) {
                        wine.amount = 0;
                        wine.subTotal = 0;
                    }

                    this.winesFilter = this.wines;
                    this.winesFilter.sort((a, b) => b.price - a.price);

                    this.countPages();

                    this.cart = JSON.parse(localStorage.getItem("cart")) || [];

                    this.loader = false;
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        countPages() {
            this.listOfPages = [];
            for (
                let i = 1;
                i <= (this.winesFilter.length / 16).toFixed(0);
                i++
            ) {
                this.listOfPages.push(i);
            }
        },
        toggleCheckbox(id) {
            const checkbox = document.getElementById(id);
            checkbox.checked = !checkbox.checked;
        },
        topWindows(page) {
            this.pageNumber = page === 0 ? 1 : page;
            window.scrollTo({
                top: 0,
                behavior: "smooth",
            });
        },
        wineDetails(wine) {
            this.wineDetail = wine;
            console.log(wine);
        },
        addToCart(wine) {
            wine.amount = 1;
            wine.subTotal = wine.price * wine.amount;
            this.cart.push(wine);
            this.saveLocalStorage();
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
        filterVineyard(winesList) {
            return this.vineyarsSelected.length >= 1
                ? winesList.filter((wine) =>
                      this.vineyarsSelected.includes(wine.vineyard)
                  )
                : winesList;
        },
        filterArea(winesList) {
            return this.areaSelected.length >= 1
                ? winesList.filter((wine) =>
                      this.areaSelected.includes(wine.area)
                  )
                : winesList;
        },
        filterVariety(winesList) {
            return this.varietySelected.length >= 1
                ? winesList.filter((wine) =>
                      this.varietySelected.includes(wine.variety)
                  )
                : winesList;
        },
        filterSparkling(wineList) {
            return ["SPARKLING", "WINE"].includes(this.typeWine)
                ? wineList.filter((wine) => wine.wineType === this.typeWine)
                : wineList;
        },
        wineFilters() {
            this.winesFilter = this.filterSparkling(this.filterVariety(this.filterArea(this.filterVineyard(this.wines))));
        },
        resetFilters() {
            this.vineyarsSelected = [];
            this.areaSelected = [];
            this.varietySelected = [];
            this.typeWine = "";
        }
    },
    computed: {
        pageNumber() {
            this.winesFilter = this.winesFilter.slice(
                (pageNumber - 1) * 16,
                pageNumber * 16
            );
            console.log(this.varietySelected);
        },

        calculatePriceOfTheCart() {
            this.priceOfTheCart = this.cart.reduce(
                (accumulatedTotal, product) =>
                    accumulatedTotal + product.subTotal,
                0
            );
        },

        
    },
    watch: {
        winesFilter() {
            this.countPages();
        },
        vineyarsSelected() {
            this.wineFilters()
        },
        areaSelected() {
            this.wineFilters()
        },
        varietySelected() {
            this.wineFilters()
        },
        typeWine() {
            this.wineFilters()
        }
    },
});