package dev.adokky

data class Box(val v: Int)

data class ComparableBox(val v: Int): Comparable<ComparableBox> {
    override fun compareTo(other: ComparableBox): Int = v.compareTo(other.v)
}

abstract class BaseBox(val v: Int) {
    override fun equals(other: Any?): Boolean = (other as? BaseBox)?.v == v
    override fun hashCode(): Int = v
    override fun toString(): String = v.toString()
}

abstract class BaseComparableBox<T: BaseComparableBox<T>>(v: Int): BaseBox(v), Comparable<T> {
    override fun compareTo(other: T): Int = v.compareTo(other.v)
}