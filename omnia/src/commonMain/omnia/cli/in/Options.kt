package omnia.cli.`in`

import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.mutable.HashMap

class Options {

  private val mutableOptions = HashMap.create<String, Option>()
  val options get() = ImmutableMap.copyOf(mutableOptions)

  fun addOption(option: Option): Options {
    require(!mutableOptions.keys.contains(option.longName) &&
        (option.shortName?.let { !mutableOptions.keys.contains(it) } ?: true))

    mutableOptions.putMapping(option.longName, option)
    option.shortName?.let { mutableOptions.putMapping(it, option) }
    return this
  }
}