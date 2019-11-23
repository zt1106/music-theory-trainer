package cc.zengtian.mtt.util

/**
 * Created by ZengTian on 2019/11/9.
 */
class Rational(
        private val numerator: Int,
        private val denominator: Int
) : Comparable<Rational> {

    init {
        if (denominator == 0) {
            throw IllegalArgumentException("Denominator cannot be 0.")
        }
    }

    operator fun plus(other: Rational): Rational {
        val num = (numerator * other.denominator) + (denominator * other.numerator)
        val den = denominator * other.denominator
        return num.divBy(den)
    }

    operator fun minus(other: Rational): Rational {
        val num = (numerator * other.denominator) - (denominator * other.numerator)
        val den = denominator * other.denominator
        return num.divBy(den)
    }

    operator fun times(other: Rational): Rational {
        val num = numerator * other.numerator
        val den = denominator * other.denominator
        return num.divBy(den)
    }

    operator fun div(other: Rational): Rational {
        val num = numerator * other.denominator
        val den = denominator * other.numerator
        return num.divBy(den)
    }

    operator fun unaryMinus(): Rational = Rational(-numerator, denominator)

    override fun compareTo(other: Rational): Int {
        return (numerator * other.denominator).compareTo(denominator * other.numerator)
    }

    fun simplify(): Rational {
        val greatestCommonDivisor = gcd(numerator, denominator)
        val num = numerator / greatestCommonDivisor
        val den = denominator / greatestCommonDivisor
        return Rational(num, den)
    }

    private fun gcd(num1: Int, num2: Int): Int {
        var n1 = num1
        var n2 = num2
        while (n1 != n2) {
            if (n1 > n2)
                n1 -= n2
            else
                n2 -= n1
        }
        return n1
    }

    private fun format(): String = "$numerator/$denominator"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other is Rational) {
            val simplifiedThis = simplify()
            val simplifiedOther = other.simplify()
            val thisAsDouble =
                    simplifiedThis.numerator.toDouble() / simplifiedThis.denominator.toDouble()
            val otherAsDouble =
                    simplifiedOther.numerator.toDouble() / simplifiedOther.denominator.toDouble()
            return thisAsDouble == otherAsDouble
        }
        return false
    }

    override fun toString(): String {
        val shouldBeOneNumberOnly =
                denominator == 1 || numerator % denominator == 0
        return when {
            shouldBeOneNumberOnly -> (numerator / denominator).toString()
            else -> {
                val simplified = simplify()
                if (simplified.denominator < 0
                        || simplified.denominator < 0) {
                    Rational(-simplified.numerator,
                            -simplified.denominator).format()
                } else {
                    Rational(simplified.numerator, simplified.denominator).format()
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = numerator
        result = 31 * result + denominator
        return result
    }
}

infix fun Int.divBy(other: Int): Rational = Rational(this, other)