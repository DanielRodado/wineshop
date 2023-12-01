const { createApp } = Vue;

createApp({

  data() {
    return {
        cardNumber: '',
        cvv: '',
        subtotal: [],
        wines: [],
        accessories: [],
        deliveryAddress: ""
    
    };
  },

  created() {

  },

  methods: {
    createPurchase() {
      let paymentInfo = []
      let purchaseInfo = []
      
      axios
        .post('/api/purchase', )
        .then()
        .catch()
    }
   
  },
}).mount('#app');

