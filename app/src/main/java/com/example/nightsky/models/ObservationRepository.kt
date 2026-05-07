package com.example.nightsky.models // Ajustado para o seu pacote

// O objeto 'object' no Kotlin cria um Singleton (uma instância única para o app todo)
object ObservationRepository {
    private val observations = mutableListOf<Observation>()
    private var nextId = 1

    init {
        // Dados iniciais para o app não começar vazio
        observations.addAll(listOf(
            Observation(nextId++, "Júpiter", "Planeta", "2024-01-15 22:30", -15.7801, -47.9292, null,
                "Avistamento claro com 4 luas galileanas visíveis. Banda equatorial bem definida."),
            Observation(nextId++, "Orion", "Constelação", "2024-01-20 21:00", -23.5505, -46.6333, null,
                "Cinturão de Orion perfeitamente visível. Nebulosa de Orion detectada a olho nu."),
            Observation(nextId++, "Saturno", "Planeta", "2024-02-03 23:15", -15.7801, -47.9292, null,
                "Anéis claramente visíveis com binóculo. Titan identificado nas proximidades."),
            Observation(nextId++, "Via Láctea", "Galáxia", "2024-02-14 00:30", -22.9068, -43.1729, null,
                "Núcleo galáctico visível. Excelente transparência atmosférica esta noite."),
            Observation(nextId++, "Marte", "Planeta", "2024-03-01 20:45", -15.7801, -47.9292, null,
                "Coloração avermelhada distinta. Calota polar norte possivelmente visível.")
        ))
    }

    // Retorna todas as observações
    fun getAll(): List<Observation> = observations

    // Adiciona uma nova observação vinda do formulário
    fun add(observation: Observation) {
        val newObs = observation.copy(id = nextId++)
        observations.add(0, newObs) // Adiciona no topo da lista (mais recente primeiro)
    }

    // Retorna a contagem total
    fun count(): Int = observations.size
}