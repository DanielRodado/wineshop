const { createApp } = Vue;

createApp({

  data() {
    return {
        cardNumber: '',
        expiryDate: '',
        cvv: '',
        cardHolder: '',
        identificationNumber: '',
        installments: '1',
        paymentMethod: ''
        
    
    };
  },

  created() {
  
   

  },

  methods: {

    payments(){
        console.log(this.paymentMethod);
    }
    // submitForm() {
    //     // agregar petición 
    //     alert('Pago procesado con éxito');
    // },

    // errorMessage(message) {
    //     Swal.fire({
    //       icon: "error",
    //       title: "An error has occurred",
    //       text: message,
    //       color: "#fff",
    //       background: "#1c2754",
    //       confirmButtonColor: "#17acc9",
    //   });
    // },
  },
}).mount('#app');

