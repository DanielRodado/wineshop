new Vue({
    el: '#app',
    data: {
        showOffer: [],
        productPrice: 329,
        productRating: "",
        quantity: 1,
        isLoginPopupOpen: false,
        username: '',
        password: '',
        wines: [],
        accessories: [],
    },

    created() {

        this.getWines();
        this.getAccessories();
    },

    methods: {

        getWines() {
            axios
                .get("/api/wines")
                .then((response) => {
                    console.log(response);
                    this.wines = response.data;
                    this.productRating = response.data.valuations;
                    this.showOffer = response.data.showOffer;
                    // this.showOfferImage = 

    
                })
                .catch((error) => {
                    console.log("error")
                    console.log(error)
                });
        },


        getAccessories() {
            axios
                .get("/api/accessories")
                .then((response) => {
                    this.accessories = response.data;
                })
                .catch((error) => {
                    console.log("error")
                    console.log(error)
                });
        },


        addToCart() {
            // Lógica para agregar al carrito
            console.log('Producto agregado al carrito');
        },
        decreaseQuantity() {
            if (this.quantity > 1) {
                this.quantity--;
            }
        },
        increaseQuantity() {
            this.quantity++;
        },

    },
    computed: {
        starRating() {
            const numberOfStars = Math.round(this.productRating);
            return '★'.repeat(numberOfStars) + '☆'.repeat(5 - numberOfStars);
        },
        openLoginPopup() {
            this.isLoginPopupOpen = true;
        },
        closeLoginPopup() {
            this.isLoginPopupOpen = false;
        },
        login() {
            // Aquí puedes agregar la lógica de autenticación con Vue.js
            alert('Login successful!');
            this.closeLoginPopup();
        }
    },
});