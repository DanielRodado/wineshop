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
            variety: "A_A"
        }
    },

    created() {
        this.getWines();
    },

    methods: {
        getWines() {
            axios
                .get("/api/wines")
                .then(({ data }) => {
                    this.wines = data;
                    this.winesFilter = data;
                    this.winesFilter.sort((a, b) => b.price - a.price);
                    this.countPages();
                    this.loader = false;
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        getAccessories() {
            axios
                .get("/api/accessories")
                .then(({ data }) => {
                    this.accessories = data;
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
        }
    },
    computed: {
        pageNumber() {
            this.winesFilter = this.winesFilter.slice((pageNumber-1)*16, pageNumber*16)
        }
    },
    watch: {
        winesFilter() {
            this.countPages();
        }
    },
});