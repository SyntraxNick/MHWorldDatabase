package com.gatheringhallstudios.mhworlddatabase.data.raw;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Entity for monster
 * Created by Carlos on 3/4/2018.
 */
@Entity(tableName = "monster")
public class MonsterRaw {
    @PrimaryKey
    public int id;

    // todo: add more data once the database has more data
}