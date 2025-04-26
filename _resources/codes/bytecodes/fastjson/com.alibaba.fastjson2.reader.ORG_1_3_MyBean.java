import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReader3;
import org.example.fastjson.MyBean;

import java.lang.reflect.Type;
import java.util.function.Supplier;

public final class ORG_1_3_MyBean extends ObjectReader3 {
    public ORG_1_3_MyBean(Class var1, Supplier var2, FieldReader[] var3) {
        super(var1, null, null, 0L, null, var2, null, var3);
    }

    public Object readObject(JSONReader var1, Type var2, Object var3, long var4) {
        if (var1.jsonb) {
            return this.readJSONBObject(var1, var2, var3, var4);
        } else {
            MyBean var6;
            if (var1.isArray()) {
                if (var1.isSupportBeanArray(var4)) {
                    var1.nextIfArrayStart();
                    MyBean var10 = new MyBean();
                    if (var1.isInitStringFieldAsEmpty()) {
                        this.initStringFieldAsEmpty(var10);
                    }

                    var6 = var10;
                    String var10003 = var1.readString();
                    if (var10003 != null) {
                    }

                    var6.id = var10003;
                    var10003 = var1.readString();
                    if (var10003 != null) {
                    }

                    var6.name = var10003;
                    var6.score = var1.readInt32Value();
                    var1.nextIfArrayEnd();
                    var1.nextIfComma();
                    return var6;
                } else {
                    return this.processObjectInputSingleItemArray(var1, var2, var3, var4);
                }
            } else {
                if (!var1.nextIfObjectStart() && var1.nextIfNullOrEmptyString()) {
                    var6 = null;
                } else {
                    MyBean var10001 = new MyBean();
                    if (var1.isInitStringFieldAsEmpty()) {
                        this.initStringFieldAsEmpty(var10001);
                    }

                    var6 = var10001;

                    for (int var7 = 0; !var1.nextIfObjectEnd(); ++var7) {
                        String var10002;
                        switch (var1.getRawInt()) {
                            case 577005858:
                                if (var1.nextIfName4Match2()) {
                                    var10002 = var1.readString();
                                    if (var10002 != null) {
                                    }

                                    var6.id = var10002;
                                    continue;
                                }
                                break;
                            case 1835101730:
                                if (var1.nextIfName4Match4((byte) 101)) {
                                    var10002 = var1.readString();
                                    if (var10002 != null) {
                                    }

                                    var6.name = var10002;
                                    continue;
                                }
                                break;
                            case 1868788514:
                                if (var1.nextIfName4Match5(975332722)) {
                                    var6.score = var1.readInt32Value();
                                    continue;
                                }
                        }

                        long var8;
                        if ((var8 = var1.readFieldNameHashCode()) == -1L) {
                            break;
                        }

                        if (var7 == 0 && var8 == 435678704704L && var1.isSupportAutoTypeOrHandler(var4)) {
                            return this.auoType(var1, this.objectClass, var4);
                        }

                        if (var8 == 25705L) {
                            var10002 = var1.readString();
                            if (var10002 != null) {
                            }

                            var6.id = var10002;
                        } else if (var8 == 1701667182L) {
                            var10002 = var1.readString();
                            if (var10002 != null) {
                            }

                            var6.name = var10002;
                        } else if (var8 == 435711599475L) {
                            var6.score = var1.readInt32Value();
                        } else {
                            if (var1.isSupportSmartMatch(var4)) {
                                var8 = var1.getNameHashCodeLCase();
                                if (var8 == 25705L) {
                                    var10002 = var1.readString();
                                    if (var10002 != null) {
                                    }

                                    var6.id = var10002;
                                    continue;
                                }

                                if (var8 == 1701667182L) {
                                    var10002 = var1.readString();
                                    if (var10002 != null) {
                                    }

                                    var6.name = var10002;
                                    continue;
                                }

                                if (var8 == 435711599475L) {
                                    var6.score = var1.readInt32Value();
                                    continue;
                                }
                            }

                            this.processExtra(var1, var6);
                        }
                    }
                }

                var1.nextIfComma();
                return var6;
            }
        }
    }

    public Object createInstance(long var1) {
        return new MyBean();
    }

    public FieldReader getFieldReader(long var1) {
        FieldReader var10000;
        if (var1 == 25705L) {
            var10000 = this.fieldReader0;
        } else if (var1 == 1701667182L) {
            var10000 = this.fieldReader1;
        } else {
            if (var1 != 435711599475L) {
                return null;
            }

            var10000 = this.fieldReader2;
        }

        return var10000;
    }

    public Object readJSONBObject(JSONReader var1, Type var2, Object var3, long var4) {
        ObjectReader var15;
        if ((var15 = this.checkAutoType(var1, var4)) != null) {
            return var15.readJSONBObject(var1, var2, var3, var4);
        } else if (var1.nextIfNull()) {
            return null;
        } else {
            var1.errorOnNoneSerializable(this.objectClass);
            MyBean var10000;
            String var10001;
            if (var1.isArray() && var1.isSupportBeanArray()) {
                var10000 = new MyBean();
                if (var1.isInitStringFieldAsEmpty()) {
                    this.initStringFieldAsEmpty(var10000);
                }

                MyBean var16 = var10000;
                int var7;
                if ((var7 = var1.startArray()) == 3) {
                    var10001 = var1.readString();
                    if (var10001 != null) {
                    }

                    var16.id = var10001;
                    var10001 = var1.readString();
                    if (var10001 != null) {
                    }

                    var16.name = var10001;
                    var16.score = var1.readInt32Value();
                } else {
                    this.readArrayMappingJSONBObject0(var1, var16, var7);
                }

                return var16;
            } else {
                var10000 = new MyBean();
                if (var1.isInitStringFieldAsEmpty()) {
                    this.initStringFieldAsEmpty(var10000);
                }

                var1.nextIfObjectStart();
                var10000 = new MyBean();
                if (var1.isInitStringFieldAsEmpty()) {
                    this.initStringFieldAsEmpty(var10000);
                }

                Object var6 = var10000;

                for (int var8 = 0; !var1.nextIfObjectEnd(); ++var8) {
                    long var9;
                    if ((var9 = var1.readFieldNameHashCode()) != 0L) {
                        if (var9 == this.typeKeyHashCode && var9 != 0L) {
                            var6 = this.autoType(var1);
                            break;
                        }

                        if (var9 == 25705L) {
                            var10001 = var1.readString();
                            if (var10001 != null) {
                            }

                            ((MyBean) var6).id = var10001;
                        } else if (var9 == 1701667182L) {
                            var10001 = var1.readString();
                            if (var10001 != null) {
                            }

                            ((MyBean) var6).name = var10001;
                        } else if (var9 == 435711599475L) {
                            ((MyBean) var6).score = var1.readInt32Value();
                        } else {
                            if (var1.isSupportSmartMatch(var4)) {
                                var9 = var1.getNameHashCodeLCase();
                                if (var9 == 25705L) {
                                    var10001 = var1.readString();
                                    if (var10001 != null) {
                                    }

                                    ((MyBean) var6).id = var10001;
                                    continue;
                                }

                                if (var9 == 1701667182L) {
                                    var10001 = var1.readString();
                                    if (var10001 != null) {
                                    }

                                    ((MyBean) var6).name = var10001;
                                    continue;
                                }

                                if (var9 == 435711599475L) {
                                    ((MyBean) var6).score = var1.readInt32Value();
                                    continue;
                                }
                            }

                            this.processExtra(var1, var6);
                        }
                    }
                }

                return var6;
            }
        }
    }

    public FieldReader getFieldReaderLCase(long var1) {
        FieldReader var10000;
        if (var1 == 25705L) {
            var10000 = this.fieldReader0;
        } else if (var1 == 1701667182L) {
            var10000 = this.fieldReader1;
        } else {
            if (var1 != 435711599475L) {
                return null;
            }

            var10000 = this.fieldReader2;
        }

        return var10000;
    }

    public Object readArrayMappingJSONBObject(JSONReader var1, Type var2, Object var3, long var4) {
        ObjectReader var10;
        if ((var10 = this.checkAutoType(var1, var4)) != null) {
            return var10.readJSONBObject(var1, var2, var3, var4);
        } else if (var1.nextIfNull()) {
            return null;
        } else {
            MyBean var10000 = new MyBean();
            if (var1.isInitStringFieldAsEmpty()) {
                this.initStringFieldAsEmpty(var10000);
            }

            MyBean var6 = var10000;
            int var7;
            if ((var7 = var1.startArray()) == 3) {
                String var10001 = var1.readString();
                if (var10001 != null) {
                }

                var6.id = var10001;
                var10001 = var1.readString();
                if (var10001 != null) {
                }

                var6.name = var10001;
                var6.score = var1.readInt32Value();
            } else {
                this.readArrayMappingJSONBObject0(var1, var6, var7);
            }

            return var6;
        }
    }
}
