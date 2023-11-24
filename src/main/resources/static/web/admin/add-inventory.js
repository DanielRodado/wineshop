const { createApp } = Vue;

createApp({

  data() {
    return {
        wines: [],
        accessories: [],

        wineName: "",
        wineDescription: "",
        wineArea: "",
        wineVineyard: "",
        winePrice: "",
        newWineImg: "",
        wineImgURLs: [],
        wineStock: 0,
        wineCC: 0,
        wineVariety: "",
        wineType: "",

        wineVarietiesList: [],

        accessoryName: "",
        accessoryDescription: "",
        newAccessoryImg: "",
        accessoryImgURLs: [],
        accessoryPrice: "",
        accessoryStock: 0,
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

    addNewWineImgURL() {
      if(this.newWineImg.length != 0){
        this.wineImgURLs.push(this.newWineImg);
        this.newWineImg = "";
      }
        
    },

    resetWineImages() {
        this.wineImgURLs = [];
    },

    addNewAccessoryImgURL() {
      this.accessoryImgURLs.push(this.newAccessoryImg);
      this.newAccessoryImg = "";
    },

    resetAccessoryImages() {
        this.accessoryImgURLs = [];
    },

    createNewWine() {
        let wineData = {
            name: this.wineName,
            description: this.wineDescription,
            area: this.wineArea,
            vineyard: this.wineVineyard,
            price: this.winePrice, 
            imgURL: this.wineImgURLs,
            stock: this.wineStock,
            cc: this.wineCC,
            variety: this.wineVariety,
            wineType: this.wineType
            };
      axios
      .post('/api/wines/create', wineData)
      .then(response => {

        Swal.fire({
          icon: "success",
          title: "Wine created",
          text: "Wine created",
          color: "#fff",
          background: "#1c2754",
          confirmButtonColor: "#17acc9",
      });
        location.pathname = '/web/admin/add-inventory.html'
      })
      .catch(error => { 
        console.error('Error:', error)
        this.errorMessage(error.response.data)
      });
    },

    createNewAccessory() {
      let accessoryData = {
          name: this.accessoryName,
          description: this.accessoryDescription,
          price: this.accessoryPrice, 
          imgURL: this.accessoryImgURLs,
          stock: this.accessoryStock,
          };
    axios
    .post('/api/accessories/create', accessoryData)
    .then(response => {

      Swal.fire({
        icon: "success",
        title: "accessory created",
        text: "accessory created",
        color: "#fff",
        background: "#1c2754",
        confirmButtonColor: "#17acc9",
    });
      location.pathname = '/web/admin/add-inventory.html'
    })
    .catch(error => { 
      console.error('Error:', error)
      this.errorMessage(error.response.data)
    });
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

