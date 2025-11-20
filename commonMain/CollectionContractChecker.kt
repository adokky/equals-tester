package dev.adokky.eqtester

internal class CollectionContractChecker: PairTesterBase() {
    fun test(config: EqualsTesterConfigImpl) {
        setup(config)

        for (gi in config.groups.indices) {
            selectGroups(gi, gi)
            selectElements(0, 0)

            if (checkMapContract()) continue
            if (checkSetContract()) continue
            if (checkListContract()) continue
        }
    }

    private fun checkListContract(): Boolean {
        val list = element1 as? List<Any?> ?: return false
        if (list.isVerified()) return false

        val copy = list.toList()
        if (list.hashCode() != copy.hashCode()) failure("breaks List.hashCode() contract")
        if (list != copy) failure("breaks List.equals() contract")
        return true
    }

    private fun checkSetContract(): Boolean {
        val set = element1 as? Set<Any?> ?: return false
        if (set.isVerified()) return false

        val copy = set.toSet()
        if (set.hashCode() != copy.hashCode()) failure("breaks Set.hashCode() contract")
        if (set != copy) failure("breaks Set.equals() contract")
        return true
    }

    private fun checkMapContract(): Boolean {
        @Suppress("UNCHECKED_CAST")
        val map = element1 as? Map<Any, Any> ?: return false
        if (map.isVerified()) return false

        val copy = map.toMap()
        if (map.hashCode() != copy.hashCode()) failure("breaks Map.hashCode() contract")
        if (map != copy) failure("breaks Map.equals() contract")
        return true
    }

    private fun List<Any?>.isVerified(): Boolean =
        this::class == ArrayList::class ||
        this::class == ArrayDeque::class

    private fun Set<Any?>.isVerified(): Boolean =
        this::class == HashSet::class ||
        this::class == LinkedHashSet::class

    private fun Map<Any, Any>.isVerified(): Boolean =
        this::class == HashMap::class ||
        this::class == LinkedHashMap::class
}

