package dev.schlaubi.lavakord.jda


/**
 * Calculates the shard id for the guild with [snowflake] as its id.
 */
internal fun JDALavakord.getShardIdForGuild(snowflake: ULong): Int =
    ((snowflake shr 22) % shardsTotal.toUInt()).toInt()
