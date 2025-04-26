import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriter3;
import org.example.fastjson.MyBean;

import java.lang.reflect.Type;
import java.util.List;

public final class OWG_1_3_MyBean extends ObjectWriter3 implements ObjectWriter {
    public OWG_1_3_MyBean(Class var1, String var2, String var3, long var4, List var6) {
        super(var1, var2, var3, var4, var6);
    }

    public void write(JSONWriter var1, Object var2, Object var3, Type var4, long var5) {
        long var8 = var1.getFeatures();
        boolean var10 = var1.utf8 && (var8 & 274878955520L) == 0L;
        long var17;
        int var12 = (var17 = (var8 & 4096L) - 0L) == 0L ? 0 : (var17 < 0L ? -1 : 1);
        if (var12 != 0) {
            boolean var13 = false;
        } else {
            long var18;
            int var16 = (var18 = (var8 & 80L) - 0L) == 0L ? 0 : (var18 < 0L ? -1 : 1);
        }

        if ((var8 & 32768L) != 0L) {
            super.write(var1, var2, var3, var4, var5);
        } else if (var1.jsonb) {
            if ((var8 & 8L) != 0L) {
                this.writeArrayMappingJSONB(var1, var2, var3, var4, var5);
            } else {
                this.writeJSONB(var1, var2, var3, var4, var5);
            }
        } else if ((var8 & 8L) != 0L) {
            this.writeArrayMapping(var1, var2, var3, var4, var5);
        } else if (this.hasFilter(var1)) {
            this.writeWithFilter(var1, var2, var3, var4, var5);
        } else {
            if ((var8 & 2L) != 0L) {
                var1.writeNull();
            } else if ((var8 & 4L) != 0L) {
                this.errorOnNoneSerializable();
            } else {
                var1.startObject();
                boolean var7 = true;
                if (var2 != null && var2.getClass() != var4 && var1.isWriteTypeInfo(var2, var4, var5)) {
                    var7 = this.writeTypeInfo(var1) ^ true;
                }

                String var14;
                byte[] var10001;
                if ((var14 = ((MyBean) var2).id) != null) {
                    if (var10) {
                        var1.writeName2Raw(249685109026L);
                    } else {
                        this.fieldWriter0.writeFieldName(var1);
                    }

                    var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var14, 20L);
                    if (JDKUtils.UNSAFE.getByte(var14, 16L) == 0) {
                        var1.writeStringLatin1(var10001);
                    } else {
                        var1.writeStringUTF16(var10001);
                    }
                } else if ((var8 & 8388688L) != 0L && (var8 & 4096L) == 0L) {
                    if (var10) {
                        var1.writeName2Raw(249685109026L);
                    } else {
                        this.fieldWriter0.writeFieldName(var1);
                    }

                    if (var1.isEnabled(8388672L)) {
                        var1.writeString("");
                    } else {
                        var1.writeStringNull();
                    }
                }

                if ((var14 = ((MyBean) var2).name) != null) {
                    if (var10) {
                        var1.writeName4Raw(16363367671361058L);
                    } else {
                        this.fieldWriter1.writeFieldName(var1);
                    }

                    var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var14, 20L);
                    if (JDKUtils.UNSAFE.getByte(var14, 16L) == 0) {
                        var1.writeStringLatin1(var10001);
                    } else {
                        var1.writeStringUTF16(var10001);
                    }
                } else if ((var8 & 8388688L) != 0L && (var8 & 4096L) == 0L) {
                    if (var10) {
                        var1.writeName4Raw(16363367671361058L);
                    } else {
                        this.fieldWriter1.writeFieldName(var1);
                    }

                    if (var1.isEnabled(8388672L)) {
                        var1.writeString("");
                    } else {
                        var1.writeStringNull();
                    }
                }

                int var15;
                if ((var15 = ((MyBean) var2).score) != 0 || var12 == 0) {
                    if (var10) {
                        var1.writeName5Raw(4189022145577448226L);
                    } else {
                        this.fieldWriter2.writeFieldName(var1);
                    }

                    var1.writeInt32(var15);
                }

                var1.endObject();
            }

        }
    }

    public void writeJSONB(JSONWriter var1, Object var2, Object var3, Type var4, long var5) {
        long var7 = var1.getFeatures();
        long var14;
        int var9 = (var14 = (var7 & 4096L) - 0L) == 0L ? 0 : (var14 < 0L ? -1 : 1);
        if (var9 != 0) {
            boolean var10 = false;
        } else {
            long var15;
            int var13 = (var15 = (var7 & 80L) - 0L) == 0L ? 0 : (var15 < 0L ? -1 : 1);
        }

        if ((var7 & 2L) != 0L) {
            var1.writeNull();
        } else if ((var7 & 4L) != 0L) {
            this.errorOnNoneSerializable();
        } else {
            if (var2 != null && var2.getClass() != var4 && var1.isWriteTypeInfo(var2, var4, var5)) {
                this.writeClassInfo(var1);
            }

            var1.startObject();
            String var11;
            byte[] var10001;
            if ((var11 = ((MyBean) var2).id) != null) {
                this.fieldWriter0.writeFieldNameJSONB(var1);
                var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var11, 20L);
                if (JDKUtils.UNSAFE.getByte(var11, 16L) == 0) {
                    var1.writeStringLatin1(var10001);
                } else {
                    var1.writeStringUTF16(var10001);
                }
            } else if ((var7 & 8388688L) != 0L && (var7 & 4096L) == 0L) {
                this.fieldWriter0.writeFieldNameJSONB(var1);
                if (var1.isEnabled(8388672L)) {
                    var1.writeString("");
                } else {
                    var1.writeStringNull();
                }
            }

            if ((var11 = ((MyBean) var2).name) != null) {
                this.fieldWriter1.writeFieldNameJSONB(var1);
                var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var11, 20L);
                if (JDKUtils.UNSAFE.getByte(var11, 16L) == 0) {
                    var1.writeStringLatin1(var10001);
                } else {
                    var1.writeStringUTF16(var10001);
                }
            } else if ((var7 & 8388688L) != 0L && (var7 & 4096L) == 0L) {
                this.fieldWriter1.writeFieldNameJSONB(var1);
                if (var1.isEnabled(8388672L)) {
                    var1.writeString("");
                } else {
                    var1.writeStringNull();
                }
            }

            int var12;
            if ((var12 = ((MyBean) var2).score) != 0 || var9 == 0) {
                this.fieldWriter2.writeFieldNameJSONB(var1);
                var1.writeInt32(var12);
            }

            var1.endObject();
        }

    }

    public void writeArrayMapping(JSONWriter var1, Object var2, Object var3, Type var4, long var5) {
        if (var1.jsonb) {
            this.writeArrayMappingJSONB(var1, var2, var3, var4, var5);
        } else if (this.hasFilter(var1)) {
            super.writeArrayMapping(var1, var2, var3, var4, var5);
        } else {
            var1.startArray();
            String var7 = (String) ((MyBean) var2).id;
            byte[] var10001;
            if (var7 == null) {
                var1.writeStringNull();
            } else {
                var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var7, 20L);
                if (JDKUtils.UNSAFE.getByte(var7, 16L) == 0) {
                    var1.writeStringLatin1(var10001);
                } else {
                    var1.writeStringUTF16(var10001);
                }
            }

            var1.writeComma();
            var7 = (String) ((MyBean) var2).name;
            if (var7 == null) {
                var1.writeStringNull();
            } else {
                var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var7, 20L);
                if (JDKUtils.UNSAFE.getByte(var7, 16L) == 0) {
                    var1.writeStringLatin1(var10001);
                } else {
                    var1.writeStringUTF16(var10001);
                }
            }

            var1.writeComma();
            var1.writeInt32(((MyBean) var2).score);
            var1.endArray();
        }
    }

    public void writeArrayMappingJSONB(JSONWriter var1, Object var2, Object var3, Type var4, long var5) {
        if (var2 != null && var2.getClass() != var4 && var1.isWriteTypeInfo(var2, var4, var5)) {
            this.writeClassInfo(var1);
        }

        var1.startArray(3);
        long var7 = var1.getFeatures();
        long var13;
        int var9 = (var13 = (var7 & 4096L) - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
        if (var9 != 0) {
            boolean var10 = false;
        } else {
            long var14;
            int var12 = (var14 = (var7 & 80L) - 0L) == 0L ? 0 : (var14 < 0L ? -1 : 1);
        }

        String var11 = (String) ((MyBean) var2).id;
        byte[] var10001;
        if (var11 == null) {
            var1.writeStringNull();
        } else {
            var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var11, 20L);
            if (JDKUtils.UNSAFE.getByte(var11, 16L) == 0) {
                var1.writeStringLatin1(var10001);
            } else {
                var1.writeStringUTF16(var10001);
            }
        }

        var11 = (String) ((MyBean) var2).name;
        if (var11 == null) {
            var1.writeStringNull();
        } else {
            var10001 = (byte[]) JDKUtils.UNSAFE.getObject(var11, 20L);
            if (JDKUtils.UNSAFE.getByte(var11, 16L) == 0) {
                var1.writeStringLatin1(var10001);
            } else {
                var1.writeStringUTF16(var10001);
            }
        }

        var1.writeInt32(((MyBean) var2).score);
    }
}
