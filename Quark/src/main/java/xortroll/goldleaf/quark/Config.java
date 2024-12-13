
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    public static Path configPath;
    private Properties innerCfg;
    private File cfgFile;

    public Config() throws Exception {
        if (configPath == null) {
            String configHome = System.getenv("XDG_CONFIG_HOME");
            if (configHome == null || configHome.trim().isEmpty()) {
                configHome = System.getProperty("user.home") + File.separator + ".config";
            }
            Path quarkConfigDirPath = Paths.get(configHome, "quark");
            if (Files.notExists(quarkConfigDirPath)) {
                Files.createDirectories(quarkConfigDirPath);
            }
            configPath = quarkConfigDirPath.resolve("quark-config.cfg");
        }
        this.innerCfg = new Properties();
        reloadConfigFile();
    }

    public void reloadConfigFile() throws Exception {
        this.cfgFile = configPath.toFile();
        if (this.cfgFile.isFile()) {
            this.innerCfg.load(new FileInputStream(this.cfgFile));
        } else {
            this.cfgFile.createNewFile();
        }
    }

    public void save() throws Exception {
        this.innerCfg.store(new FileOutputStream(this.cfgFile), null);
    }

    public Properties getContent() {
        return this.innerCfg;
    }
}