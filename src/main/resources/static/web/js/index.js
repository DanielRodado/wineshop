const { createApp } = Vue;

createApp({

  data() {
    return {
        wines: [],
        wineVarietiesList:[],
        vineyards:new Set(),
        areas:new Set(),
        varieties:new Set(),
        sparkling:[],
        wineSparkling:new Set(),
        email:"",
        password:"",
        infoLogin:"",
        showLogin: true,
        name: "",
        lastName:"",
        infoRegister:"",
        errorEmail:false,
    };
  },

  created() {
  //   $(document).ready(function() {

  //     $('.counter').each(function () {
  // $(this).prop('Counter',0).animate({
  //     Counter: $(this).text()
  // }, {
  //     duration: 4000,
  //     easing: 'swing',
  //     step: function (now) {
  //         $(this).text(Math.ceil(now));
  //     }
  // });
  // });
  
  // }); 


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
                this.vineyards.add(wine.vineyard);
                
          };
          this.vineyards = Array.from(this.vineyards).sort((vineyard1, vineyard2) => {
            return vineyard1.localeCompare(vineyard2)});

          for (let wine of this.wines){
            this.areas.add(wine.area.trim())
          };
          this.areas = Array.from(this.areas).sort((area1, area2) => {
            return area1.localeCompare(area2)});

            for (let wine of this.wines){
              this.varieties.add(wine.variety.trim())
            };
            this.varieties = Array.from(this.varieties).sort((variety1, variety2) => {
              return variety1.localeCompare(variety2)});

            this.sparkling = this.wines.filter((wine => {return wine.wineType=="SPARKLING"}));
           this.sparkling.forEach(wine => {
            wine.variety = wine.variety[0] + wine.variety.substring(1).toLowerCase()
            

           });
            
            for(let wine of this.sparkling){
              this.wineSparkling.add(wine.variety)
            };
            this.wineSparkling = Array.from(this.wineSparkling).sort((variety1,variety2) => {
              return variety1.localeCompare(variety2)});
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
    
    catalogue(){
     location.href='/web/pages/catalogue.html'
    },
    
    login(){
      this.errorEmail = !this.validateEmail(this.email);
      this.infoLogin = `email=${this.email}&password=${this.password}`;

      axios.post('/api/login', this.infoLogin)
        .then(response => {
          console.log("Successful request", response.data);
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Welcome',
            showConfirmButton: false,
            timer: 2000
          })
          setTimeout(()=> {
            location.href = '/web/pages/catalogue.html';
          },3000);

        })
        .catch(err => {
          console.log(err);
          if (this.email = "" || this.password === "") {
            Swal.fire({
                icon: "error",
                title: "Error...",
                text: "Please complete all information",
                showConfirmButton: false,
                timer: 2000
            });
        } else {
            Swal.fire({
                icon: "error",
                title: "Invalid user",
                text: "This user is not registered",
                showConfirmButton: false,
                timer: 2000
            });
          }
          
         
      })
      .finally(() => {
          this.email = "";
          this.password = "";
      });
  },
  register(){
    this.errorEmail = !this.validateEmail(this.email);
    this.infoRegister = `name=${this.name}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`;
    console.log(this.infoRegister);
    axios.post('/api/clients',this.infoRegister)
    .then(response => {
     
      console.log('registered')
      
      Swal.fire({
        position: 'center',
        icon: 'success',
        title: 'Your register was successful',
        showConfirmButton: false,
        timer:2000
      })
      console.log(this.login());
  

    })
    .catch(err => {
      console.log(err)
      if (this.email = "" || this.password === "") {
        Swal.fire({
            icon: "error",
            title: "Error...",
            text: "Fill in all fields",
            confirmButtonColor: false,
            timer: 2000
        });
    } else {
        Swal.fire({
            icon: "error",
            title: "Invalid user",
            text: "This user is not registered",
            confirmButtonColor: false,
            timer:2000
        });
      }
    })
    .finally(() => {
      this.email = "";
      this.password = "";
      this.name = "";
      this.lastName = "";
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

