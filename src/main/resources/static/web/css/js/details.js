new Vue({
    el: '#app',
    data: {
        id:"",
        wines: [],
        accessories: []
    },

    created() {

        this.id =  new URLSearchParams(location.search).get("id");
        this.loadAccount();



    },

    methods: {
        loadWines() {
            axios
                .get("/api/wines/current")
                .then((response) => {
                    console.log(response);
                    this.wine = response.data.find(a => a.id == this.id);
                })
                .catch((error) => {
                    console.log("error")
                    console.log(error)
                });
        },

    },
    computed: {
    //     starRating() {
    //         const numberOfStars = Math.round(this.productRating);
    //         return '★'.repeat(numberOfStars) + '☆'.repeat(5 - numberOfStars);
    //     },
    //     openLoginPopup() {
    //         this.isLoginPopupOpen = true;
    //     },
    //     closeLoginPopup() {
    //         this.isLoginPopupOpen = false;
    //     },
    //     login() {
    //         // Aquí puedes agregar la lógica de autenticación con Vue.js
    //         alert('Login successful!');
    //         this.closeLoginPopup();
    //     }
     },
});