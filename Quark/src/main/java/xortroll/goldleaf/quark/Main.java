
/*

    Goldleaf - Multipurpose homebrew tool for Nintendo Switch
    Copyright (C) 2018-2023 XorTroll

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

*/

package xortroll.goldleaf.quark;

import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import xortroll.goldleaf.quark.ui.MainApplication;

public class Main {

  private static final String CONFIG_FILE_OPTION = "cfgfile";

  public static void main(String[] args) throws ParseException {
    Options options = new Options().addOption(CONFIG_FILE_OPTION, true, "Config file");

    CommandLine cmd =  new DefaultParser().parse(options, args);
    if (cmd.hasOption(CONFIG_FILE_OPTION)) {
      Config.configPath = Paths.get(cmd.getOptionValue(CONFIG_FILE_OPTION));
    }
    
    MainApplication.run(args);
  }
}