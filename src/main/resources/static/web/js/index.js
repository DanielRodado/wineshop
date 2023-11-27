const { createApp } = Vue;

createApp({

  data() {
    return {
        wines: [],
        wineVarietiesList:[],
        vineyards:new Set()
    };
  },

  created() {
    axios
        .get('/api/wines/varieties')
        .then((response) => {
            this.wineVarietiesList = response.data;
        })
        .catch((error) => {
          console.log("error")
          console.log(error)
        });
    
    axios
        .get("/api/wines")
        .then((response) => {
          this.wines = response.data;
          console.log(this.wines);
          for (let wine of this.wines){
                this.vineyards.add(wine.vineyard)
          }
          console.log(this.vineyards);
        })
        .catch((error) => {
          console.log("error")
          console.log(error)
        });
    
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

  methods: {
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

