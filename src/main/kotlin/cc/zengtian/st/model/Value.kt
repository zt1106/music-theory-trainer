package cc.zengtian.st.model

/**
 * Created by ZengTian on 2019/9/8.
 */
val IONIAN = ScaleSteps("IONIAN", listOf(2, 2, 1, 2, 2, 2))
val DORIAN = ScaleSteps("DORIAN", listOf(2, 1, 2, 2, 2, 1))
val PHRYGIAN = ScaleSteps("PHRYGIAN", listOf(1, 2, 2, 2, 1, 2))
val LYDIAN = ScaleSteps("LYDIAN", listOf(2, 2, 2, 1, 2, 2))
val MIXOLYDIAN = ScaleSteps("MIXOLYDIAN", listOf(2, 2, 1, 2, 2, 1))
val AEOLIAN = ScaleSteps("AEOLIAN", listOf(2, 1, 2, 2, 1, 2))
val LOCRIAN = ScaleSteps("LOCRIAN", listOf(1, 2, 2, 1, 2, 2))
val HARMONIC_MINOR = ScaleSteps("HARMONIC_MINOR", listOf(2, 1, 2, 2, 1, 3))
val MELODIC_MINOR_UPPER = ScaleSteps("MELODIC_MINOR_UPPER", listOf(2, 1, 2, 2, 2, 2))
val PENTATONIC = ScaleSteps("PENTATONIC", listOf(2, 2, 3, 2))
val CHROMATIC = ScaleSteps("CHROMATIC", listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1))

val BUILT_IN_SCALE_STEPS = listOf(
    IONIAN,
    DORIAN,
    PHRYGIAN,
    LYDIAN,
    MIXOLYDIAN,
    AEOLIAN,
    LOCRIAN,
    HARMONIC_MINOR,
    MELODIC_MINOR_UPPER,
    PENTATONIC,
    CHROMATIC
)
