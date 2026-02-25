package com.example.petpawsdemo.model

import com.example.petpawsdemo.algorithm.searchCost
import java.util.TreeSet
import kotlin.math.min

class ExampleProducts {
    companion object {
        private val Image_PetPawsDogFood_Thumbnail =
            "https://media.istockphoto.com/id/539071535/photo/bowl-of-dog-food.jpg?s=612x612&w=0&k=20&c=48jSoNa5Vod-1inwbhpSQWKv5eEIhnWr8YAfhKI823M="
        private val Image_PetPawsDogFood_Premium_Thumbnail =
            "https://www.kohepets.com.sg/cdn/shop/files/science-diet-adult-small-paws-11-dry-dog-food.jpg?v=1707196979"
        private val Image_PetPawsDogTreats_Thumbnail =
            "https://okonomikitchen.com/wp-content/uploads/2019/09/vegan-pumpkin-dog-treats-recipe-1-of-1-1024x683.jpg"
        private val Image_PetPawsWetDogFood_Thumbnail =
            "https://www.tilda.com/en-in/wp-content/uploads/sites/21/2021/05/chicken-fried-rice-low-res-2.png"
        private val Image_PetPawsDogCage_Thumbnail =
            "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcSpWZMc_-DeArs8vPjvs_rkdHUs0VFAF-4Vgpif5BC9qYbW2fs8F6EwagFA_NvjMwJHcGN0Yza8REfPdAXW4rnepOLgyYefzm3g6498lK9YuHY5AEcqzSpc7w"
        private val Image_PetPawsDogToys_Thumbnail = "https://www.nocciolatoys.com/cdn/shop/files/1_284bbc55-05bb-44b0-b898-d17f05b86aa2.jpg?v=1745916563"
        private val Image_DogMansion_Thumbnail = "https://media.istockphoto.com/id/506903162/photo/luxurious-villa-with-pool.jpg?s=612x612&w=0&k=20&c=Ek2P0DQ9nHQero4m9mdDyCVMVq3TLnXigxNPcZbgX2E="
        private val Image_CatHome_Thumbnail = "https://m.media-amazon.com/images/I/81FCzJHz3WL.jpg"
        private val Image_CatMulti_Thumbnail = "https://m.media-amazon.com/images/I/81xJiu-NaOL._AC_UF894,1000_QL80_.jpg"
        private val Image_CatNailClippers_Thumbnail = "https://m.media-amazon.com/images/I/71QgOhNWw1L._AC_UF1000,1000_QL80_.jpg"
        private val Image_TurtleFood_Thumbnail = "https://www.petmart.sg/assets/product_image/368512081-ZM-51B-Aquatic-Turtle-Fd.jpeg"
        val PetPawsDogFood = Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog", "food"),
            mutableListOf("nutritious", "dog", "food"),
            67,
            1899,
            0.0,
            0,
            mutableListOf(Image_PetPawsDogFood_Thumbnail),
            "Pet Paws",
            0.2f,
            "A balanced, nutrient-rich dry dog food formulated to support your dog’s overall health, energy levels, and digestive wellness. Made with quality ingredients to keep your pet active and satisfied every day."
        )

        val PetPawsDogFoodPremium = Product(
            "Pet Paws Premium Dry Dog Food",
            ProductCategory("dog", "food"),
            mutableListOf("dog", "food", "premium", "dry"),
            43,
            4599,
            0.0,
            0,
            mutableListOf(Image_PetPawsDogFood_Premium_Thumbnail),
            "Pet Paws",
            0.2f,
            "High-protein premium dry dog food crafted for optimal muscle development and sustained energy. Enriched with essential vitamins and minerals to promote a healthy coat, strong bones, and overall vitality."
        )

        val PetPawsDogFoodBudget = Product(
            "Delicious Vegetarian Dog Treats",
            ProductCategory("dog", "food"),
            mutableListOf("dog", "treats", "vegetarian", "non-meat", "delicious", "food"),
            10,
            799,
            0.0,
            0,
            mutableListOf(Image_PetPawsDogTreats_Thumbnail),
            "Pet Paws",
            0.2f,
            "Tasty vegetarian dog treats made with wholesome plant-based ingredients for a guilt-free reward. Perfect for training sessions or everyday snacking without compromising on flavor."
        )

        val PetPawsWetDogFood = Product(
            "Pet Paws Wet Dog Food – Chicken & Rice",
            ProductCategory("dog", "food"),
            mutableListOf("dog", "food", "wet", "chicken", "rice"),
            98,
            249,
            0.0,
            0,
            mutableListOf(Image_PetPawsWetDogFood_Thumbnail),
            "Pet Paws",
            0.2f,
            "Tender chicken and rice in a rich, savory gravy designed for easy digestion and enhanced palatability. Ideal for picky eaters or as a nutritious complement to dry food."
        )

        val PetPawsDogCage = Product(
            "Pet Paws Secure Dog Crate",
            ProductCategory("dog","cage"),
            mutableListOf("dog", "cage", "crate"),
            3,
            8999,
            0.0,
            0,
            mutableListOf(Image_PetPawsDogCage_Thumbnail, Image_PetPawsDogTreats_Thumbnail, Image_PetPawsWetDogFood_Thumbnail),
            "Pet Paws",
            0.2f,
            "Durable and secure dog crate designed to provide a safe, comfortable space for rest and training. Features sturdy construction and proper ventilation to ensure your pet’s safety and comfort."
        )

        val PetPawsDogToys = Product(
            "Pet Paws Assorted Colorful Dog Toys",
            ProductCategory("dog", "toys"),
            mutableListOf("dog", "toys", "fun", "assorted", "colorful"),
            21364,
            1999,
            0.0,
            0,
            mutableListOf(Image_PetPawsDogToys_Thumbnail),
            "Pet Paws",
            0.2f,
            "A vibrant assortment of durable dog toys designed to keep your pet mentally stimulated and physically active. Great for chewing, fetching, and interactive play sessions."
        )

        val PetPawsDogMansion = Product(
            "Pet Paws Elite Luxury Dog Mansion",
            ProductCategory("dog", "cage"),
            mutableListOf("dog", "cage", "mansion", "luxury"),
            1,
            249999,
            0.0,
            0,
            mutableListOf(Image_DogMansion_Thumbnail),
            "Pet Paws Elite",
            0.05f,
            "An ultra-premium luxury dog mansion offering exceptional space, comfort, and elegant architectural design. Built with high-end materials to provide your pet with a secure and sophisticated living environment."
        )

        val PetPawsCatHome = Product(
            "Pet Paws Budget Cardboard Cat Home",
            ProductCategory("cat", "cage"),
            listOf("cat", "cage", "budget", "cardboard", "spacious"),
            100,
            999,
            0.0,
            1000,
            listOf(Image_CatHome_Thumbnail),
            "Pet Paws",
            0.05f,
            "A lightweight and affordable cardboard cat home that offers a cozy retreat for lounging and play. Easy to assemble and spacious enough to keep your cat comfortable and entertained."
        )

        val PetPawsCatCage = Product(
            "Pet Paws Multi-Storey Cat Cage",
            ProductCategory("cat", "cage"),
            listOf("cat", "cage", "multi", "storey"),
            10,
            3999,
            0.0,
            100,
            listOf(Image_CatMulti_Thumbnail),
            "Pet Paws",
            0.05f,
            "A premium cat cage."
        )

        val PetPawsNailClippers = Product(
            "Pet Paws Nail Clippers",
            ProductCategory("cat", "grooming"),
            listOf("cat", "grooming", "clipper", "nail", "safe"),
            100,
            399,
            0.0,
            100,
            listOf(Image_CatNailClippers_Thumbnail),
            "Pet Paws",
            0.15f,
            "Durable stainless steel nail clippers designed for safe, precise trimming to keep your pet’s nails neat and healthy. Features an ergonomic non-slip handle for better control and a built-in safety guard to help prevent over-cutting."
        )

        val PetPawsTurtleFood = Product(
            "Pet Paws Turtle Food",
            ProductCategory("turtle", "food"),
            listOf("turtle", "food", "delicious"),
            100,
            1399,
            0.0,
            100,
            listOf(Image_TurtleFood_Thumbnail),
            "ZOOMED",
            0.15f,
            "The #1 aquatic turtle food is available in 3 pellet sizes and protein levels (Hatchling, Growth, and Maintenance Formulas). Each has been scientifically formulated to meet the dietary requirements of aquatic turtles at each of their life stages. This Growth formula meets the dietary requirements for growing aquatic turtles. Our pellets float making it easier for aquatic turtles, who prefer eating at the water surface, to locate their food."
        )
    }
}

object ProductDatabase{
    private val products: MutableMap<Int, Product> = mutableMapOf()
    private val productIDMap: MutableMap<Product, Int> = mutableMapOf()
    private val unused: TreeSet<Int> = TreeSet<Int>().apply{ add(0) }

    fun getProduct(id: Int) = products[id]
    fun getID(product: Product): Int? {
        val id = productIDMap[product]
        if (id != null) return id
        //fallback to search by name in case id not applicable
        return productIDMap.entries.find { it.key.name == product.name }?.value
    }

    fun getRating(id: Int) = this.products[id]?.rating

    fun addProduct(product: Product){
        val id = unused.pollFirst()
        if (unused.isEmpty()){
            unused.add(id + 1)
        }
        products[id] = product
        productIDMap[product] = id
    }

    fun removeProduct(id: Int){
        val product = getProduct(id)
        products.remove(id)
        if (product != null) productIDMap.remove(product)
        unused.add(id)
    }

    fun updateProduct(product: Product) {
        val id = getID(product)
        if (id != null) {
            products[id] = product
            //update key in map & remove old key to realise property changes
            productIDMap.keys.find { it.name == product.name }?.let { oldKey ->
                productIDMap.remove(oldKey)
            }
            productIDMap[product] = id
        }
    }

    fun getProducts() = products as Map<Int, Product>

    fun getProductIDMap() = productIDMap as Map<Product, Int>

    fun getProductSet() = products.values

    fun getIDset() = productIDMap.values

    fun getAll(): Map<String, Map<String, List<Product>>>{
        return getProductSet().sortedBy{it.productCategory.type}.groupBy{it.productCategory.type}.mapValues{it.value.sortedBy{it.productCategory.subtype}.groupBy{it.productCategory.subtype}}
    }

    fun getCategory(type: String): Map<String, List<Product>> {
        return getProductSet().filter{it.productCategory.type == type}.sortedBy{it.productCategory.subtype}.groupBy{it.productCategory.subtype}
    }

    fun search(query: String): List<Product>{
        val queryWords = query.split(" ").map{it.lowercase()}
        val products = getProductSet().map{it.searchCost(queryWords) to it}
        val res = products.sortedBy{it.first}
        val firstMatch = res.firstOrNull() ?: return emptyList()
        val threshold = firstMatch.first + 50
        return res.filter{it.first <= min(threshold, 1000)}.map{it.second}
    }

    fun getCategoryMap() = getProductSet().map{it.productCategory}.groupBy{it.type}

    fun getSubcategory(category: ProductCategory): List<Product> = getProductSet().filter{it.productCategory.type == category.type && it.productCategory.subtype == category.subtype}

    init{
        mutableListOf(
            ExampleProducts.PetPawsDogCage,
            ExampleProducts.PetPawsDogFood,
            ExampleProducts.PetPawsDogToys,
            ExampleProducts.PetPawsWetDogFood,
            ExampleProducts.PetPawsDogFoodPremium,
            ExampleProducts.PetPawsDogFoodBudget,
            ExampleProducts.PetPawsDogMansion,
            ExampleProducts.PetPawsCatHome,
            ExampleProducts.PetPawsCatCage,
            ExampleProducts.PetPawsNailClippers,
            ExampleProducts.PetPawsTurtleFood
        ).forEach{prod ->
            addProduct(prod)
        }
    }
}