new Vue({
    el: "#app",
    data: {
        wines: [],
        winesFilter: [],
        loader: true,
        listOfPages: [],
        pageNumber: 1,
        pagesN: 1,
        wineDetail: {
            imgURL: ["a", "b"],
            stock: 2,
            price: 2000,
            variety: "A_A",
        },
        cart: [],
        priceOfTheCart: 0,
        vineyarsSelected: [],
        areaSelected: [],
        varietySelected: [],
        typeWine: "",
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
        this.getWines();
    },

    methods: {
        getWines() {
            axios
                .get("/api/wines")
                .then(({ data }) => {
                    this.wines = [...data];

                    for (let wine of this.wines) {
                        wine.amount = 0;
                        wine.subTotal = 0;
                    }

                    this.winesFilter = this.wines;
                    this.winesFilter.sort((a, b) => b.price - a.price);

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
                i <= (this.winesFilter.length / 16).toFixed(0);
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
        wineDetails(wine) {
            this.wineDetail = wine;
            console.log(wine);
        },
        addToCart(wine) {
            wine.amount = 1;
            wine.subTotal = wine.price * wine.amount;
            this.cart.push(wine);
            this.saveLocalStorage();
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
        },
        filterVineyard(winesList) {
            return this.vineyarsSelected.length >= 1
                ? winesList.filter((wine) =>
                      this.vineyarsSelected.includes(wine.vineyard)
                  )
                : winesList;
        },
        filterArea(winesList) {
            return this.areaSelected.length >= 1
                ? winesList.filter((wine) =>
                      this.areaSelected.includes(wine.area)
                  )
                : winesList;
        },
        filterVariety(winesList) {
            return this.varietySelected.length >= 1
                ? winesList.filter((wine) =>
                      this.varietySelected.includes(wine.variety)
                  )
                : winesList;
        },
        filterSparkling(wineList) {
            return ["SPARKLING", "WINE"].includes(this.typeWine)
                ? wineList.filter((wine) => wine.wineType === this.typeWine)
                : wineList;
        },
        wineFilters() {
            this.winesFilter = this.filterSparkling(this.filterVariety(this.filterArea(this.filterVineyard(this.wines))));
        },
        resetFilters() {
            this.vineyarsSelected = [];
            this.areaSelected = [];
            this.varietySelected = [];
            this.typeWine = "";
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
            this.winesFilter = this.winesFilter.slice(
                (pageNumber - 1) * 16,
                pageNumber * 16
            );
            console.log(this.varietySelected);
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
        winesFilter() {
            this.countPages();
        },
        vineyarsSelected() {
            this.wineFilters()
        },
        areaSelected() {
            this.wineFilters()
        },
        varietySelected() {
            this.wineFilters()
        },
        typeWine() {
            this.wineFilters()
        }
    },
});