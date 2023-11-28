new Vue({
    el: '#app',
    data: {
        id:"",
        wines: [],
        accessories: [],
        quantity: 1
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