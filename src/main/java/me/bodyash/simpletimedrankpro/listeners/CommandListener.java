package me.bodyash.simpletimedrankpro.listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.bodyash.simpletimedrankpro.Main;
import me.bodyash.simpletimedrankpro.dao.ConfigUser;
import me.bodyash.simpletimedrankpro.dao.User;
import me.bodyash.simpletimedrankpro.utils.ConfigUtil;

public class CommandListener{
	
	private ConfigUtil config;
	private Main main;
	private ConfigUser configUser;
	private TimeChecker timeChecker;
	
	public CommandListener(ConfigUtil config, Main main, ConfigUser configUser, TimeChecker timeChecker) {
		this.config = config;
		this.main = main;
		this.configUser = configUser;
		this.timeChecker = timeChecker;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("strp")) {
			this.strp(sender, command, label, args);
			return true;
		}
		if (label.equalsIgnoreCase("tempRank") || label.equalsIgnoreCase("upRank")) {
			this.tempRank(sender, command, label, args);
			return true;
		}
		if (label.equalsIgnoreCase("downRank")) {
			this.downRank(sender, command, label, args);
			return true;
		}
		if (label.equalsIgnoreCase("timeLeft") || label.equalsIgnoreCase("daysLeft")
				|| label.equalsIgnoreCase("dayLeft")) {
			this.timeLeft(sender, command, label, args);
			return true;
		}
		return false;
	}

	private boolean timeLeft(CommandSender sender, Command command, String label, String[] args) {
		block16: {
		block17: {
			block18: {
				block12: {
					block13: {
						block15: {
								block10: {
									block11: {
										try {
											if (args.length <= 1)
												break block10;
											if (!sender.hasPermission("simpletimedrank.help"))
												break block11;
											sender.sendMessage(String.valueOf(config.getChatLogo()) + (Object) ChatColor.GOLD
													+ "If you need help with the commands type '/strp help'.");
											return false;
										} catch (Exception e) {
											sender.sendMessage(
													String.valueOf(config.getChatLogo()) + "Error: Can't check the time!");
											return false;
										}
									}
									sender.sendMessage(config.getNoPermMessage());
									return false;
								}
								if (args.length != 0)
									break block12;
								if (!sender.hasPermission("simpletimedrank.timeLeft"))
									break block13;
								Long days = this.timeChecker.getPlayerDaysLeft(sender.getName());
								if (days == null){
									sender.sendMessage(config.getChatLogo() + config.getCantCheckTimeMsg());
									return true;
								}
								if(days == 0) {
									sender.sendMessage(String.valueOf(config.getChatLogo()) + "Time left as "
											+ this.configUser.getUserData(sender.getName()).getPromotedRank() + ": " + timeLeftString(this.configUser.getUserData(sender.getName()).getUntilDate()));
									return true;
								}
								if (days >0) {
									sender.sendMessage(String.valueOf(config.getChatLogo()) + "Time left as "
											+ this.configUser.getUserData(sender.getName()).getPromotedRank() + ": " + days
											+ " Day(s)");
									return true;
								}

							if (String.valueOf(this.configUser.getUserData(sender.getName()).getStatus())
									.compareToIgnoreCase("-1") != 0)
								break block15;
							sender.sendMessage(String.valueOf(config.getChatLogo()) + config.getTimeExpiredMsg());
							return false;
						}
						sender.sendMessage(String.valueOf(config.getChatLogo()) + config.getCantCheckTimeMsg());
						return false;
					}
					sender.sendMessage(config.getNoPermMessage());
					return false;
				}
				if (args.length != 1)
					break block16;
				if (!sender.hasPermission("simpletimedrank.timeLeft.others"))
					break block17;
				Long days = this.timeChecker.getPlayerDaysLeft(args[0]);
				if (days == null)
					break block18;
				sender.sendMessage(String.valueOf(config.getChatLogo()) + "Time left " + days + " Days");
				return true;
			}
			sender.sendMessage(String.valueOf(config.getChatLogo()) + "Can't find the player " + args[0] + ".");
			return false;
		}
		sender.sendMessage(config.getNoPermMessage());
		return false;
	}
	return false;
		
	}

	private boolean downRank(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("simpletimedrank.downRank")) {
			if (args.length < 1 || args.length > 1) {
				if (sender.hasPermission("simpletimedrank.help")) {
					sender.sendMessage(String.valueOf(config.getChatLogo()) + (Object) ChatColor.GOLD
							+ "If you need help with the commands type '/str help'.");
					return false;
				}
				sender.sendMessage(config.getNoPermMessage());
				return false;
			}
			if (args.length == 1) {
				try {
					if (this.configUser.getUserData(args[0]).getOldRank() != null
							&& String.valueOf(this.configUser.getUserData(args[0]).getStatus()).compareToIgnoreCase("-1") != 0) {
						this.configUser.setUserTimeExpired(args[0]);
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								(String) this.parseSyntax(this.configUser.getUserData(args[0]), this.config.getDemoteCommand()));
						return true;
					}
					sender.sendMessage(String.valueOf(config.getChatLogo()) + "Can't demote the player " + args[0]
							+ " because the player isn't promoted and/or isn't registered!");
					return false;
				} catch (Exception e) {
					try {
						sender.sendMessage(String.valueOf(config.getChatLogo()) + "Can't demote the player " + args[0]
								+ " because the player isn't promoted and/or isn't registered!");
						return false;
					} catch (Exception e1) {
						System.err.println(String.valueOf(config.getConsoleLogo())
								+ "An error occurred while performing the command downRank!");
						return false;
					}
				}
			}
		}
		sender.sendMessage(config.getNoPermMessage());
		return false;
		
	}

	private boolean tempRank(CommandSender sender, Command command, String label, String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat(config.getDateFormat() + " HH:mm");
		if (args.length < 4 || args.length > 5) {
			if (sender.hasPermission("simpletimedrank.help")) {
				sender.sendMessage(String.valueOf(config.getChatLogo()) + (Object) ChatColor.GOLD
						+ "If you need help with the commands type '/strp help'.");
				sender.sendMessage(String.valueOf(config.getChatLogo()) + (Object) ChatColor.GOLD
						+ "Correct usage: /tempRank [Player] [NewRank] [DaysOrMonths] (timeOfDay) [OldRank]");
				return false;
			}
			sender.sendMessage(config.getNoPermMessage());
			return false;
		}
		if (sender.hasPermission("simpletimedrank.tempRank")) {
			if (args.length == 4) {
				if (this.checkIfCorrectDate(args[2])) {
					try {
						User tempUser = new User(args[0], parseNumsAndLatters(args[2]), new Date().getTime(), args[1], args[3], 1);
						this.configUser.addUser(tempUser);
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								(String) this.parseSyntax(tempUser, config.getPromoteCommand()));
						sender.sendMessage(String.valueOf(config.getChatLogo()) + "The player " + args[0]
								+ " has been promoted to the rank " + args[1] + " until "
								+ sdf.format(new Date(parseNumsAndLatters(args[2])))
								+ "!");
						System.out.println(String.valueOf(config.getConsoleLogo()) + "The player " + args[0]
								+ " has been promoted from the player " + sender.getName() + " from " + args[3] + " to "
								+ args[1] + " until "
								+ sdf.format(new Date(parseNumsAndLatters(args[2])))
								+ "!");
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						sender.sendMessage(String.valueOf(config.getChatLogo())
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						sender.sendMessage(String.valueOf(config.getChatLogo()) + "Error: The player was not promoted.");
						System.err.println(String.valueOf(config.getConsoleLogo())
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						System.err.println(String.valueOf(config.getConsoleLogo()) + "Error: The player was not promoted.");
						return false;
					}
				}
				sender.sendMessage(String.valueOf(config.getChatLogo()) + "Correct date format: 5d or 2m (5 days, 2 months)");
				sender.sendMessage(String.valueOf(config.getChatLogo()) + "Example: /temprank Player Vip 25d Guest");
			}
			if (args.length == 5) {
				if (this.checkIfCorrectDate(args[2]) && this.checkIfCorrectTime(args[3])) {
					try {
						User tempUser = new User(args[0], parseNumsWithTime(args[2], args[3]), new Date().getTime(), args[1], args[4], 1);
						this.configUser.addUser(tempUser);
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								(String) this.parseSyntax(tempUser, config.getPromoteCommand()));
						sender.sendMessage(String.valueOf(config.getChatLogo()) + "The player " + args[0]
								+ " has been promoted to the rank " + args[1] + " until "
								+ sdf.format(new Date(this.parseNumsWithTime(args[2], args[3])))
								+ "!");
						System.out.println(String.valueOf(config.getConsoleLogo()) + "The player " + args[0]
								+ " has been promoted from the player " + sender.getName() + " from " + args[4] + " to "
								+ args[1] + " until "
								+ sdf.format(new Date(this.parseNumsWithTime(args[2], args[3])))
								+ "!");
						return true;
					} catch (Exception e) {
						sender.sendMessage(String.valueOf(config.getChatLogo())
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						sender.sendMessage(String.valueOf(config.getChatLogo()) + "Error: The player was not promoted.");
						System.err.println(String.valueOf(config.getConsoleLogo())
								+ "Error: An error occurred while trying to promote the player " + args[0] + "!");
						System.err.println(String.valueOf(config.getConsoleLogo()) + "Error: The player was not promoted.");
						return false;
					}
				}
				if (!this.checkIfCorrectDate(args[2])) {
					sender.sendMessage(String.valueOf(config.getChatLogo()) + "Correct date format: d or m (Days and Monthes)");
				}
				if (!this.checkIfCorrectTime(args[3])) {
					sender.sendMessage(String.valueOf(config.getChatLogo()) + "Correct time format: hours:minutes");
				}
				sender.sendMessage(String.valueOf(config.getChatLogo()) + "Example: /temprank Player Vip 14d 20:12 Guest");
			}
		} else {
			sender.sendMessage(config.getNoPermMessage());
			return false;
		}
		return false;
		
	}

	private long parseNumsWithTime(String stringDate, String stringTime) {
		GregorianCalendar greg = new GregorianCalendar();
		greg.setTime(new Date());
		stringDate.toLowerCase();
		int days = 0;
		if (stringDate.endsWith("m")) {
			if (stringDate.length() >= 3) {
				days = Integer.parseInt(stringDate.substring(0, 2)) * 30;
			} else if (stringDate.length() == 2) {
				days = Integer.parseInt(stringDate.substring(0, 1)) * 30;
			}
		}

		if (stringDate.endsWith("d")) {
			days = Integer.parseInt(stringDate.substring(0, stringDate.length() - 1));
		}

		greg.add(Calendar.DAY_OF_YEAR, days);
		int hours = 0;
		int minutes = 0;
		StringTokenizer stringArgs;
		if (stringTime.contains(":")){
			try {
				stringArgs = new StringTokenizer(stringTime, ":");
				hours = Integer.parseInt(stringArgs.nextToken());
				if (!(hours >= 0 && hours <= 23)){
					hours = greg.get(Calendar.HOUR_OF_DAY);
				}
				minutes = Integer.parseInt(stringArgs.nextToken());
				if ((minutes < 0 || minutes > 59)) {
					minutes = greg.get(Calendar.MINUTE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		greg.set(Calendar.HOUR_OF_DAY, hours);
		greg.set(Calendar.MINUTE, minutes);
		return greg.getTimeInMillis();
	
	}

	private boolean checkIfCorrectTime(String stringTimeToCheck) {
		StringTokenizer args;
		if (stringTimeToCheck.contains(":")) {
			block4: {
				try {
					args = new StringTokenizer(stringTimeToCheck, ":");
					int hours = Integer.parseInt(args.nextToken());
					if (hours >= 0 && hours <= 23)
						break block4;
					return false;
				} catch (Exception e) {
					return false;
				}
			}
			try {
				int minutes = Integer.parseInt(args.nextToken());
				if (minutes < 0 || minutes > 59) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	private long parseNumsAndLatters(String stringDate) {
		GregorianCalendar greg = new GregorianCalendar();
		greg.setTime(new Date());
		stringDate.toLowerCase();
		int days = 0;
		if (stringDate.endsWith("m")) {
			if (stringDate.length() >= 3) {
				days = Integer.parseInt(stringDate.substring(0, 2)) * 30;
			} else if (stringDate.length() == 2) {
				days = Integer.parseInt(stringDate.substring(0, 1)) * 30;
			}
		}

		if (stringDate.endsWith("d")) {
			days = Integer.parseInt(stringDate.substring(0, stringDate.length() - 1));
		}

		greg.add(Calendar.DAY_OF_YEAR, days);

		return greg.getTimeInMillis();
	}

	private boolean strp(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("simpletimedrank.help")) {
				sender.sendMessage(config.getChatLogo() +  ChatColor.GOLD
						+ "If you need help with the commands type '/str help'.");
				return false;
			}
			sender.sendMessage(config.getNoPermMessage());
			return false;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help")) {
				if (sender.hasPermission("simpletimedrank.help")) {
					this.listAllCommands(sender);
					return true;
				}
				sender.sendMessage(String.valueOf(config.getChatLogo()) + config.getNoPermMessage());
				return false;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("simpletimedrank.reload")) {
					this.onReload(sender);
					sender.sendMessage(String.valueOf(config.getChatLogo()) + (Object) ChatColor.DARK_GREEN + "Plugin Reloaded !");
					return true;
				}
				sender.sendMessage(String.valueOf(config.getChatLogo()) + config.getNoPermMessage());
				return false;
			}
		}
		if (sender.hasPermission("simpletimedrank.help")) {
			sender.sendMessage(String.valueOf(config.getChatLogo()) + (Object) ChatColor.GOLD
					+ "If you need help with the commands type '/str help'.");
			return false;
		}
		sender.sendMessage(String.valueOf(config.getChatLogo()) + config.getNoPermMessage());
		return false;
		
	}

	private boolean onReload(CommandSender sender) {
		main.getServer().getPluginManager().disablePlugin(main);
		main.getServer().getPluginManager().enablePlugin(main);
		return true;
		
	}

	private void listAllCommands(CommandSender sender) {
		int i = 1;
		sender.sendMessage(
				String.valueOf(config.getChatLogo()) + ChatColor.DARK_GREEN + "--------- Command List ---------");
		String left = ChatColor.GREEN + "[";
		String right = ChatColor.GREEN + "]";
		if (sender.hasPermission("simpletimedrank.help")) {
			sender.sendMessage(String.valueOf(left) + i + right + this.changeColor(i)
					+ " /strp help: Prints you the commands of this plugin");
			++i;
		}
		if (sender.hasPermission("simpletimedrank.reload")) {
			sender.sendMessage(String.valueOf(left) + i + right + this.changeColor(i)
					+ " /strp reload: Reloads the whole plugin.");
			++i;
		}
		if (sender.hasPermission("simpletimedrank.tempRank")) {
			sender.sendMessage(String.valueOf(left) + i + right + this.changeColor(i)
					+ " /tempRank [Player] [NewRank] [DaysOrMonths] (timeOfDay) [OldRank]: Promotes a player to [NewRank] until [DaysOrMonths] (timeOfDay).");
			++i;
		}
		if (sender.hasPermission("simpletimedrank.downRank")) {
			sender.sendMessage(String.valueOf(left) + i + right + this.changeColor(i)
					+ " /downRank [Player]: Demotes a Player to his old rank.");
			++i;
		}
		if (sender.hasPermission("simpletimedrank.timeLeft")) {
			sender.sendMessage(String.valueOf(left) + i + right + this.changeColor(i)
					+ " /daysLeft: Shows the remaining time until your rank will expire.");
			++i;
		}
		
	}
	
	private String timeLeftString(Long until){
		long allMinutes = TimeUnit.MILLISECONDS.toMinutes(until - new Date().getTime());
		long minutes = allMinutes % 60;
		long hours = allMinutes / 60;
		return "" + hours + ":" + minutes;
	}
	
	private String parseSyntax(User u, String msg) {
		msg = msg.replace("%player%", u.getUserName());
		msg = msg.replace("%newRank%", u.getPromotedRank());
		msg = msg.replace("%oldRank%", u.getOldRank());
		return msg;
	}
	
	private ChatColor changeColor(int i) {
		ChatColor c1 = ChatColor.GOLD;
		ChatColor c2 = ChatColor.YELLOW;
		int id = i % 2;
		switch (id) {
		case 1: {
			return c1;
		}
		}
		return c2;
	}
	
	private boolean checkIfCorrectDate(String stringDateToCheck) {
		int temp = 0;
		if (stringDateToCheck.endsWith("d") || stringDateToCheck.endsWith("m")) {
			if (stringDateToCheck.length() > 0 && stringDateToCheck.length() < 3) {
				try {

					if (stringDateToCheck.endsWith("m")) {
						if (stringDateToCheck.length() >= 3) {
							temp = Integer.parseInt(stringDateToCheck.substring(0, 2)) * 30;
						} else if (stringDateToCheck.length() == 2) {
							temp = Integer.parseInt(stringDateToCheck.substring(0, 1)) * 30;
						}
					}

					if (stringDateToCheck.endsWith("d")) {
						temp = Integer.parseInt(stringDateToCheck.substring(0, stringDateToCheck.length() - 1));
					}

				} catch (Exception e) {
					temp = temp + 1;//Must Fix Warning in Eclipse :D Clear Code
					return false;
				}
			}
		}
		return true;
	}
	
}
