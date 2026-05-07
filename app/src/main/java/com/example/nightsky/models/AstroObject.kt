package com.example.nightsky.models
import com.example.nightsky.R

data class AstroObject(
    val name: String,
    val type: String,
    val description: String,
    val visibility: String,
    val iconRes: Int
)

object AstroDatabase {
    fun getObjects(): List<AstroObject> = listOf(
        AstroObject("Júpiter", "🪐 Planeta", "O maior planeta do sistema solar. Possui 95 luas conhecidas e a icônica Grande Mancha Vermelha.", "✨ Visível a olho nu", R.drawable.ic_planet),
        AstroObject("Saturno", "🪐 Planeta", "Famoso por seus magníficos anéis de gelo e rocha. Possui Titan, sua maior lua com atmosfera densa.", "✨ Visível a olho nu", R.drawable.ic_planet),
        AstroObject("Marte", "🪐 Planeta", "O Planeta Vermelho. Rico em óxido de ferro. Possui as maiores montanhas e o maior cânyon do sistema solar.", "✨ Visível a olho nu", R.drawable.ic_planet),
        AstroObject("Vênus", "🪐 Planeta", "O objeto mais brilhante no céu noturno após a Lua. Rotação retrógrada e temperaturas de 465°C.", "⭐ Muito brilhante", R.drawable.ic_planet),
        // ... (o restante dos planetas)
        AstroObject("Via Láctea", "🌌 Galáxia", "Nossa galáxia espiral. Contém entre 100-400 bilhões de estrelas. Núcleo visível de abril a outubro.", "✨ Céu escuro", R.drawable.ic_galaxy),
        AstroObject("Andrômeda (M31)", "🌌 Galáxia", "A galáxia espiral mais próxima de nós, a 2,537 milhões de anos-luz. Visível a olho nu.", "🔭 Céu muito escuro", R.drawable.ic_galaxy),
        AstroObject("Halley", "☄️ Cometa", "O cometa periódico mais famoso. Próxima passagem prevista para 2061. Período de 75-76 anos.", "🔭 Raro", R.drawable.ic_comet),
        AstroObject("Sírius", "⭐ Estrela", "A estrela mais brilhante do céu noturno. Parte da constelação do Cão Maior, a 8,6 anos-luz.", "⭐ Muito brilhante", R.drawable.ic_star_icon)
    )
}