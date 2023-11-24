new Vue({
    el: '#app',
    data: {
        showOfferImage: true,
        productPrice: 329,
        productRating: 100,
        quantity: 1,
        isLoginPopupOpen: false,
        username: '',
        password: '',
        wines:[],
        accessories:[]
    },

    created(){

        this.getWines();
        this.getAccessories();
        
        
        
    },

    methods: {

        getWines(){
            axios
            .get("/api/wines")
            .then((response) => {
            this.wines = response.data;
            })
            .catch((error) => {
                console.log("error")
                console.log(error)
        });
        },

        getAccessories(){
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
        }
    },
    computed: {
        starRating() {
            const numberOfStars = Math.round(this.productRating / 20);
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