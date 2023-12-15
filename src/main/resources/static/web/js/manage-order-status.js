const { createApp } = Vue;

createApp({

  data() {
    return {
        orderId: 0,
        orderNotFound: false,
        orderRecieved: false,
        orderStatus: "",
        newOrderStatus: "",
    };
  },

  created() {

  },

  methods: {

    getOrderStatus() {
        this.orderNotFound = false
        axios
            .get(`/api/purchase/status/admin`, {
                params: {
                    purchaseId: this.orderId
                }
            })
            .then((response) => {
                this.orderStatus = response.data
                this.orderRecieved = true
            })
            .catch(error => {
                this.orderNotFound = true
                this.orderStatus = error.response.data
            })

    },

    updateStatus(){
      axios
        .patch("/api/purchase/status", `purchaseId=${this.orderId}&purchaseStatus=${this.newOrderStatus}`)
        .then(response => {
          Swal.fire({
            icon: "success",
            title: "Order status changed",
            text: `Status set to ${this.newOrderStatus}`,
            color: "#fff",
            background: "#1c2754",
            confirmButtonColor: "#17acc9",
        });
        setTimeout(() => location.pathname = "/web/admin/manage-order-status.html", 3000)
        })
        .catch(error =>{
          this.errorMessage(error.response.data)
        })
    },

    errorMessage(message) {
        Swal.fire({
          icon: "error",
          title: "An error has occurred",
          text: message,
          color: "#fff",
          background: "#1c2754",
          confirmButtonColor: "#17acc9",
      });
    },
  },
}).mount('#app');