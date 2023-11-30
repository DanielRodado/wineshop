new Vue({
    el: "#app",
    data: {
        wines: [],
        winesFilter: [],
        loader: true,
        listOfPages:[]
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
            for (let i = 1; i <= (this.winesFilter.length / 16).toFixed(0); i++) {
                this.listOfPages.push(i);
            }
        },
    },
    computed: {},
    watch: {
        winesFilter() {
            this.countPages();
        },
    },
});
