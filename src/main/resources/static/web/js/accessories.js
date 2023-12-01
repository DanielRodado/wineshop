const { createApp } = Vue;

createApp({
  data() {
    return {
      wines: [],
      wineVarietiesList: [],
      vineyards: new Set(),
      areas: new Set(),
      varieties: new Set(),
      sparkling: [],
      wineSparkling: new Set(),
      accessories:[],
      email: "",
      password: "",
      showLogin: true,
      errorEmail: false,
      registerBirthDate: "",
      registerName: "",
      registerLastName: "",
      registerPass: "",
      registerEmail: "",
    };
  },

  created() {
      

    

    axios
      .get("/api/wines/varieties")
      .then((response) => {
        this.wineVarietiesList = response.data;
      })
      .catch((error) => {
        console.log("error");
        console.log(error);
      });

    axios
      .get("/api/wines")
      .then((response) => {
        this.wines = response.data;
        console.log(this.wines);
        for (let wine of this.wines) {
          this.vineyards.add(wine.vineyard);
        }
        this.vineyards = Array.from(this.vineyards).sort(
          (vineyard1, vineyard2) => {
            return vineyard1.localeCompare(vineyard2);
          }
        );

        for (let wine of this.wines) {
          this.areas.add(wine.area.trim());
        }
        this.areas = Array.from(this.areas).sort((area1, area2) => {
          return area1.localeCompare(area2);
        });

        for (let wine of this.wines) {
          const formattedVariety = wine.variety.trim().toLowerCase().replace(/^\w/, (c) => c.toUpperCase());
          this.varieties.add(formattedVariety);
        }
        this.varieties = Array.from(this.varieties).sort(
          (variety1, variety2) => {
            return variety1.localeCompare(variety2);
          }
        );

        this.sparkling = this.wines.filter((wine) => {
          return wine.wineType == "SPARKLING";
        });
        this.sparkling.forEach((wine) => {
          wine.variety =
            wine.variety[0] + wine.variety.substring(1).toLowerCase();
        });

        for (let wine of this.sparkling) {
          this.wineSparkling.add(wine.variety);
        }
        this.wineSparkling = Array.from(this.wineSparkling).sort(
          (variety1, variety2) => {
            return variety1.localeCompare(variety2);
          }
        );
      })

      .catch((error) => {
        console.log("error");
        console.log(error);
      });

    axios
      .get("/api/accessories")
      .then((response) => {
        this.accessories = response.data;
        console.log(this.accessories)



      })
      .catch((error) => {
        console.log("error");
        console.log(error);
      });
  },

  methods: {
    messageError(message) {
      Swal.fire({
        icon: "error",
        title: "<span style='color: #000;'>An error has occurred</span>",
        text: message,
        customClass: {
          popup: "text-center",
        },
        titleColor: "#000",
        color: "#000",
        background: "#fff",
        confirmButtonColor: "#880000",
      });
    },

    catalogue() {
      location.pathname = "/web/pages/catalo.html";
    },

    login() {
      
      let infoLogin = `email=${this.email}&password=${this.password}`;

      axios
        .post("/api/login", infoLogin)
        .then((response) => {
          console.log("Successful request", response.data);

          Swal.fire({
            position: "center",
            icon: "success",
            title: "Welcome",
            showConfirmButton: false,
            timer: 2000,
          });
          setTimeout(() => {
            location.pathname = "/web/pages/catalo.html";
          }, 1500);
        })
        .catch((err) => {
          console.log(err);
          if ((this.email = "" || this.password === "")) {
            Swal.fire({
              icon: "error",
              title: "Error...",
              text: "Please complete all information",
              showConfirmButton: false,
              timer: 2000,
            });
          } else {
            Swal.fire({
              icon: "error",
              title: "Invalid user",
              text: "User or password is invalid",
              showConfirmButton: false,
              timer: 2000,
            });
          }
        })
        .finally(() => {
          this.email = "";
          this.password = "";
        });
    },

    logOut(){
      axios.post('/api/logout')
      .then(response => {
        console.log('signed out!!!');
        Swal.fire({
          position: 'center',
          icon: 'warning',
          title: 'Your session has been closed',
          showConfirmButton: false,
          timer: 2000
        })
        setTimeout(()=> {
          window.location.href = '/index.html';
        },3000);
        

      })
      .catch(err=>console.log("error"))
     }, 

    register() {
      
      let registerInfo = {
        firstName: this.registerName,
        lastName: this.registerLastName,
        email: this.registerEmail,
        password: this.registerPass,
        birthDate: this.registerBirthDate,
      };
      console.log(registerInfo);

      axios
        .post("/api/clients", registerInfo)

        .then((response) => {
          console.log("registered");

          

          
          let infoLogin = `email=${this.registerEmail}&password=${this.registerPass}`;

          axios.post("/api/login", infoLogin).then((response) => {
            

            location.pathname = "/web/pages/catalo.html";
          });
        })
        .catch((err) => {
          console.log(err);
          this.messageError(err.response.data);
        })
        .finally(() => {
          this.email = "";
          this.password = "";
          this.name = "";
          this.lastName = "";
          this.birthDate = "";
        });
    },
   

    
  },
}).mount("#app");