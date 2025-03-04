/**
 * MIT License
 * <p>
 * Copyright (c) 2021 - 2023 Kyle Hicks
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.untouchedodin0.privatemines.utils.placeholderapi;

import java.util.Objects;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.untouchedodin0.kotlin.mine.data.MineData;
import me.untouchedodin0.kotlin.mine.storage.MineStorage;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.mine.Mine;
import me.untouchedodin0.privatemines.utils.QueueUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.misc.LocationUtils;

public class PrivateMinesExpansion extends PlaceholderExpansion {

  @Override
  public @NotNull String getAuthor() {
    return "UntouchedOdin0";
  }

  @Override
  public @NotNull String getIdentifier() {
    return "privatemines";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0.0";
  }

  @Override
  public boolean persist() {
    return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
  }

  @Override
  public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {

    PrivateMines privateMines = PrivateMines.getPrivateMines();
    QueueUtils queueUtils = privateMines.getQueueUtils();
    Player player = offlinePlayer.getPlayer();
    MineStorage mineStorage = privateMines.getMineStorage();
    Mine mine;

    if (player != null) {
      mine = mineStorage.get(Objects.requireNonNull(player.getPlayer()).getUniqueId());

      Location location = player.getPlayer().getLocation();

      switch (params.toLowerCase()) {
        case "size":
          if (mine != null) {
            MineData mineData = mine.getMineData();
            Location minimum = mineData.getMinimumMining();
            Location maximum = mineData.getMaximumMining();
            double distance = maximum.distance(minimum);
            int distanceInt = (int) distance;

            return Integer.toString(distanceInt);
          }
        case "owner":
          Mine closest = mineStorage.getClosest(location);
          if (closest != null) {
            MineData mineData = closest.getMineData();
            return String.valueOf(mineData.getMineOwner());
          }
          break;
        case "location":
          Mine mine1 = mineStorage.get(player);
          if (mine1 != null) {
            return String.valueOf(mine1.getLocation());
          }
        case "spawn":
          Mine mine2 = mineStorage.get(player);
          if (mine2 != null) {
            return LocationUtils.toString(mine2.getSpawnLocation());
          }
        case "inqueue":
          return String.valueOf(queueUtils.isInQueue(player.getUniqueId()));
        case "hasmine":
          return String.valueOf(mineStorage.hasMine(player));
      }
    }

    return null; // Placeholder is unknown by the Expansion
  }
}