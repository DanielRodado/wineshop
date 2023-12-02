const { createApp } = Vue;

createApp({

  data() {
    return {
        cardNumber: '',
        cvv: '',
        checkoutSubtotal: 0,
        wines: [],
        accessories: [],
        deliveryAddress: "",
        cart: []
    };
  },

  created() {
    this.cart = JSON.parse(localStorage.getItem("cart")) || [];
    // buscar una forma de separar en wines y accessories
    
  },

  methods: {
    createPurchase() {
      this.wines = this.cart.filter(product => product.hasOwnProperty("area"));
      this.accessories = this.cart.filter(product => !product.hasOwnProperty("area"));

      let paymentInfo = {
        numberCard: this.cardNumber,
        cvvCard: this.cvv,
        description: "Wine Lovers Society payment",
        amount: this.checkoutSubtotal + 2.99
      }
      let purchaseInfo = {
        deliveryAddress: this.deliveryAddress,
        wines: this.wines.map(wine => {
          return {
            productId: wine.id,
            amount: wine.amount
          }
        }),
        accessories: this.accessories.map(accessory => {
          return {
            productId: accessory.id,
            amount: accessory.amount
          }
        }),
      }

      let purchaseDTO = {
        newPurchaseApp: purchaseInfo,
        payWithCardApp: paymentInfo
      }

      axios
        .post('/api/purchase', purchaseDTO)
        .then(response => {
          console.log(response)
        })
        .catch(error => console.log(error))
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
    }
  },
  computed: {
    updateSubtotal() {
      this.checkoutSubtotal = this.cart.reduce(
        (accumulatedTotal, product) =>
            accumulatedTotal + product.subTotal,
        0
      );
    }
  }
}).mount('#app');

