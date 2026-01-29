package com.example.petpawsdemo

import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.ProductCategory
import com.example.petpawsdemo.algorithm.searchCost
import java.util.TreeSet

class ExampleProducts {
    companion object {
        private val Image_PetPawsDogFood_Thumbnail =
            "https://media.istockphoto.com/id/539071535/photo/bowl-of-dog-food.jpg?s=612x612&w=0&k=20&c=48jSoNa5Vod-1inwbhpSQWKv5eEIhnWr8YAfhKI823M="
        val PetPawsDogFood = Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog", "food"),
            listOf("dog", "food"),
            67,
            1520,
            3.4,
            100,
            listOf(),
            listOf(Image_PetPawsDogFood_Thumbnail),
            "me",
            "Dog Food to appease your dogs"
        )

        private val Image_PetPawsDogFood_Premium_Thumbnail =
            "https://www.kohepets.com.sg/cdn/shop/files/science-diet-adult-small-paws-11-dry-dog-food.jpg?v=1707196979"
        val PetPawsDogFoodPremium = Product(
            "Pet Paws Premium Dry Dog Food",
            ProductCategory("dog", "food"),
            listOf("dog", "food", "premium"),
            43,
            2899,
            4.8,
            40,
            listOf(),
            listOf(Image_PetPawsDogFood_Premium_Thumbnail),
            "me",
            "High-protein premium dry food for adult dogs"
        )

        private val Image_PetPawsDogTreats_Thumbnail =
            "https://okonomikitchen.com/wp-content/uploads/2019/09/vegan-pumpkin-dog-treats-recipe-1-of-1-1024x683.jpg"
        val PetPawsDogFoodBudget = Product(
            "Delicious Vegetarian Dog Treats",
            ProductCategory("dog", "food"),
            listOf("dog", "treats", "vegetarian", "non-meat"),
            10,
            999,
            3.9,
            120,
            listOf(),
            listOf(Image_PetPawsDogTreats_Thumbnail),
            "me",
            "Affordable dog treats"
        )

        private val Image_PetPawsWetDogFood_Thumbnail =
            "https://www.tilda.com/en-in/wp-content/uploads/sites/21/2021/05/chicken-fried-rice-low-res-2.png"
        val PetPawsWetDogFood = Product(
            "Pet Paws Wet Dog Food – Chicken & Rice",
            ProductCategory("dog", "food"),
            listOf("dog", "food", "wet"),
            98,
            249,
            4.4,
            200,
            listOf(),
            listOf(Image_PetPawsWetDogFood_Thumbnail),
            "me",
            "Moist and flavorful wet food with real chicken"
        )
        private val Image_PetPawsDogCage_Thumbnail =
            "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcSpWZMc_-DeArs8vPjvs_rkdHUs0VFAF-4Vgpif5BC9qYbW2fs8F6EwagFA_NvjMwJHcGN0Yza8REfPdAXW4rnepOLgyYefzm3g6498lK9YuHY5AEcqzSpc7w"
        val PetPawsDogCage = Product(
            "Dog Cage",
            ProductCategory("dog","cage"),
            listOf("dog", "cage"),
            3,
            1,
            4.5,
            1,
            listOf(),
            listOf(Image_PetPawsDogCage_Thumbnail,Image_PetPawsDogTreats_Thumbnail,Image_PetPawsWetDogFood_Thumbnail),
            "me",
            "Dog Cage to appease your dogs"
        )
        private val Image_PetPawsDogToys_Thumbnail = "https://www.nocciolatoys.com/cdn/shop/files/1_284bbc55-05bb-44b0-b898-d17f05b86aa2.jpg?v=1745916563"
        val PetPawsDogToys = Product(
            "Assorted Colorful Dog Toys",
            ProductCategory("dog", "toys"),
            listOf("dog", "toys", "fun"),
            21364,
            2100,
            5.0,
            10,
            listOf(),
            listOf(Image_PetPawsDogToys_Thumbnail),
            "me",
            "Dog Toys to appease your dogs"
        )

        private val Image_DogMansion_Thumbnail = "https://media.istockphoto.com/id/506903162/photo/luxurious-villa-with-pool.jpg?s=612x612&w=0&k=20&c=Ek2P0DQ9nHQero4m9mdDyCVMVq3TLnXigxNPcZbgX2E="
        val PetPawsDogMansion = Product(
            "Super Duper Premium Dog Mansion",
            ProductCategory("dog", "cage"),
            listOf("dog", "cage", "mansion", "premium"),
            1,
            210000,
            0.5,
            67,
            listOf(),
            listOf(Image_DogMansion_Thumbnail),
            "me",
            "Dog Mansion To Appease Your Dogs"
        )
    }
}

object ProductDatabase{
    private val products: MutableMap<Int, Product> = mutableMapOf()
    private val productIDMap: MutableMap<Product, Int> = mutableMapOf()
    private val unused: TreeSet<Int> = TreeSet<Int>().apply{ add(0) }

    fun getProduct(id: Int) = products[id]
    fun getID(product: Product) = productIDMap[product]

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
        productIDMap.remove(product)
        unused.add(id)
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
        return res.map{it.second}
    }

    init{
        listOf(
            ExampleProducts.PetPawsDogCage,
            ExampleProducts.PetPawsDogFood,
            ExampleProducts.PetPawsDogToys,
            ExampleProducts.PetPawsWetDogFood,
            ExampleProducts.PetPawsDogFoodPremium,
            ExampleProducts.PetPawsDogFoodBudget,
            ExampleProducts.PetPawsDogMansion
        ).forEach{prod ->
            addProduct(prod)
        }
    }
}
