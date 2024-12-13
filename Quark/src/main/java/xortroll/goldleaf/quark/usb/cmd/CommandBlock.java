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

package xortroll.goldleaf.quark.usb.cmd;

import java.nio.charset.StandardCharsets;
import xortroll.goldleaf.quark.Buffer;
import xortroll.goldleaf.quark.usb.USBInterface;

public class CommandBlock {
    private static final int BLOCK_SIZE = 0x1000;
    private static final int INPUT_MAGIC = 0x49434C47; // 'GLCI'
    private static final int OUTPUT_MAGIC = 0x4F434C47; // 'GLCO'
    private static final int RESULT_SUCCESS = 0;
    private static final int INVALID_COMMAND_ID = 0;

    private final byte[] responseBlock;
    private final Buffer responseBuffer;
    private final USBInterface usbInterface;
    private Buffer innerBuffer;

    public CommandBlock(USBInterface usbInterface) {
        this.usbInterface = usbInterface;
        byte[] innerBlock = this.usbInterface.readBytes(BLOCK_SIZE);
        if (innerBlock != null) {
            this.innerBuffer = new Buffer(innerBlock);
        }
        this.responseBlock = new byte[BLOCK_SIZE];
        this.responseBuffer = new Buffer(this.responseBlock);
    }

    public boolean isValid() {
        return this.innerBuffer != null;
    }

    public String readString() {
        int stringLength = this.read32();
        return new String(this.innerBuffer.readBytes(stringLength), StandardCharsets.UTF_8);
    }

    public int read32() {
        return this.innerBuffer.read32();
    }

    public long read64() {
        return this.innerBuffer.read64();
    }

    public void write32(int val) {
        this.responseBuffer.write32(val);
    }

    public void write64(long val) {
        this.responseBuffer.write64(val);
    }

    public void writeString(String val) {
        byte[] raw = val.getBytes(StandardCharsets.UTF_8);
        this.write32(raw.length);
        this.responseBuffer.writeBytes(raw);
    }

    public void sendBuffer(byte[] buf) {
        this.usbInterface.writeBytes(buf);
    }

    public byte[] getBuffer(int len) {
        return this.usbInterface.readBytes(len);
    }

    public int validateCommand() {
        int input_magic = this.read32();
        if (input_magic == INPUT_MAGIC) {
            int cmd_id = this.read32();
            return cmd_id;
        } else {
            return INVALID_COMMAND_ID;
        }
    }

    public void responseStart() {
        this.responseBuffer.write32(OUTPUT_MAGIC);
        this.responseBuffer.write32(RESULT_SUCCESS);
    }

    public void responseEnd() {
        this.usbInterface.writeBytes(this.responseBlock);
    }

    public void respondFailure(int rc) {
        responseBuffer.write32(OUTPUT_MAGIC);
        responseBuffer.write32(rc);
        responseEnd();
    }

    public void respondEmpty() {
        responseStart();
        responseEnd();
    }
}