package com.example.petpawsdemo.ProductClasses

import java.util.TreeSet

class ExampleProducts {
    companion object {
        private val Image_PetPawsDogFood_Thumbnail =
            "https://media.istockphoto.com/id/539071535/photo/bowl-of-dog-food.jpg?s=612x612&w=0&k=20&c=48jSoNa5Vod-1inwbhpSQWKv5eEIhnWr8YAfhKI823M="
        val PetPawsDogFood = Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("Dog", "Food"),
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
        private val Image_PetPawsDogCage_Thumbnail =
            "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcSpWZMc_-DeArs8vPjvs_rkdHUs0VFAF-4Vgpif5BC9qYbW2fs8F6EwagFA_NvjMwJHcGN0Yza8REfPdAXW4rnepOLgyYefzm3g6498lK9YuHY5AEcqzSpc7w"
        val PetPawsDogCage = Product(
            "Dog Cage",
            ProductCategory("Dog","Cage"),
            listOf("dog", "cage"),
            3,
            1,
            4.5,
            1,
            listOf(),
            listOf(Image_PetPawsDogCage_Thumbnail),
            "me",
            "Dog Cage to appease your dogs"
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
}

fun main(){
    val products = listOf(
        Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog","food"),
            listOf("dog", "food"),
            67,
            1520,
            3.4,
            100,
            listOf(),
            listOf(),
            "me",
            "Dog Food to appease your dogs"
        ),
        Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog","food"),
            listOf("dog", "food"),
            67,
            1520,
            3.4,
            100,
            listOf(),
            listOf(),
            "me",
            "Dog Food to appease your dogs"
        ),
        Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog","food"),
            listOf("dog", "food"),
            67,
            1520,
            3.4,
            100,
            listOf(),
            listOf(),
            "me",
            "Dog Food to appease your dogs"
        ),
        Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog","food"),
            listOf("dog", "food"),
            67,
            1520,
            3.4,
            100,
            listOf(),
            listOf(),
            "me",
            "Dog Food to appease your dogs"
        )
    )
    products.forEach{
        ProductDatabase.addProduct(it)
    }
    println(ProductDatabase.getCategory("dog"))
}