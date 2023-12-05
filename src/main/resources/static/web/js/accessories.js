new Vue({
  el: "#app",
  data: {
      accessories: [],
      accessoriesFilter: [],
      loader: true,
      listOfPages: [],
      pageNumber: 1,
      pagesN: 1,
      accessoryDetail: {
          imgURL: ["a", "b"],
          stock: 2,
          price: 2000,
          variety: "A_A",
      },
      cart: [],
      priceOfTheCart: 0,
      email: "",
      password: "",
      showLogin: true,
      errorEmail: false,
      registerBirthDate: "",
      registerName: "",
      registerLastName: "",
      registerPass: "",
      registerEmail: "",
  },

  created() {
      this.getAccessories();
  },

  methods: {
      getAccessories() {
          axios
              .get("/api/accessories")
              .then(({ data }) => {
                  this.accessories = [...data];

                  for (let accessory of this.accessories) {
                      accessory.amount = 0;
                      accessory.subTotal = 0;
                  }

                  this.accessoriesFilter = this.accessories;
                  this.accessoriesFilter.sort((a, b) => b.price - a.price);

                  this.countPages();

                  this.cart = JSON.parse(localStorage.getItem("cart")) || [];

                  this.loader = false;
              })
              .catch((error) => {
                  console.log(error);
              });
      },
      countPages() {
          this.listOfPages = [];
          for (
              let i = 1;
              i <= (this.accessoriesFilter.length / 16).toFixed(0);
              i++
          ) {
              this.listOfPages.push(i);
          }
      },
      toggleCheckbox(id) {
          const checkbox = document.getElementById(id);
          checkbox.checked = !checkbox.checked;
      },
      topWindows(page) {
          this.pageNumber = page === 0 ? 1 : page;
          window.scrollTo({
              top: 0,
              behavior: "smooth",
          });
      },
      accessoryDetails(accessory) {
          this.accessoryDetail = accessory;
          console.log(accessory);
      },
      addToCart(accessory) {
          accessory.amount = 1;
          accessory.subTotal = accessory.price * accessory.amount;
          this.cart.push(accessory);
          this.saveLocalStorage();
      },
      addMoreWinesToCart(accessory) {
          accessory.amount++;
          accessory.subTotal = accessory.price * accessory.amount;
          this.cart.push();
          this.saveLocalStorage();
      },
      substractOneWine(accessory) {
          accessory.amount--;
          accessory.subTotal = accessory.price * accessory.amount;
          if (accessory.amount === 0) {
              this.cart = this.cart.filter((accessories) => accessories.amount >= 1);
          }
          this.cart.push();
          this.saveLocalStorage();
      },
      deleteWineFromCart(accessory) {
          accessory.amount = 0;
          accessory.subTotal = 0;
          this.cart = this.cart.filter((accessories) => accessories.id !== wine.id);
          this.saveLocalStorage();
      },
      saveLocalStorage() {
          localStorage.setItem("cart", JSON.stringify(this.cart));
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
                location.pathname = "/web/pages/catalogue.html";
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
                
    
                location.pathname = "/web/pages/catalogue.html";
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
  computed: {
      pageNumber() {
          this.accessoriesFilter = this.accessoriesFilter.slice(
              (pageNumber - 1) * 16,
              pageNumber * 16
          );
      },

      calculatePriceOfTheCart() {
          this.priceOfTheCart = this.cart.reduce(
              (accumulatedTotal, product) =>
                  accumulatedTotal + product.subTotal,
              0
          );
      },

      
  },
  watch: {
  },
});