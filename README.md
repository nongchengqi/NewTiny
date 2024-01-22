# **NewTiny**
an image compression framework base on Tiny.

基于Tiny修改的一个轻量级图片压缩库


----
Base on: https://github.com/Sunzxyong/Tiny

修改点：

1.使用了最新的libjpeg-turbo进行编译（ver3.0.1）

2.增加了x86/x86_64架构的支持

3.修复了谷歌上架X509TrustManager的警告

4.迁移到了AndroidX,使用最新版的AGP和Gradle 8.+

5.NDK的编译由MK文件迁移到cmake实现

Modification points:
1. Compiled using the latest libjpeg-turbo (ver3.0.1)
2. Added support for x86/x86_64 architecture
3. Fixed the warning of X509TrustManager listed on Google
4. Migrated to AndroidX, using the latest version of AGP and Gradle 8.+
5. NDK compilation is implemented by migrating MK files to cmake

The usage method is consistent with the original library, please refer to the following instructions:

[使用方法和原库保持一致，请参考以下说明：]

----

## **Effect of compression**

| ImageInfo | Tiny | Wechat |
| :-: | :-: | :-: |
6.66MB (3500x2156) | 151KB (1280x788) | 135KB (1280x788)|
4.28MB (4160x3120) | 219KB (1280x960)| 195KB (1280x960)|
2.60MB (4032x3024) | 193KB (1280x960)| 173KB (1280x960)|
372KB (500x500) | 38.67KB (500x500) | 34.05KB (500x500)|
236KB (960x1280) | 127KB (960x1280) | 118KB (960x1280)|

## **Introduce**
`Tiny` does not depend on any library , it keeps the code clean on architecture . `Tiny` also uses an asynchronous thread pool to compress images , and will hand out the result in the main thread when compression is completed.

## **Usage**
### **Installation**

```
implementation(files("src/main/libs/newtiny-release_v1.0.0.aar"))
```

### **Choose an abi**
**Tiny** provide abi：`armeabi-v7a`、`arm64-v8a`、`x86_64`、`x86`.

Choose what you need **"abi"** version：

```
android {
    defaultConfig {
        ndk {
            abiFilters 'armeabi-v7a','x86'//or arm64-v8a、x86_64
        }
    }
}
```

### **Initialization**

```
        //Tiny.getInstance().init(this); //unnecessary
```
### **Compression**

#### **AsBitmap**

```
        Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
        //options.height = xxx;//some compression configuration.
        Tiny.getInstance().source("").asBitmap().withOptions(options).compress(new BitmapCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, Throwable t) {
                //return the compressed bitmap object
            }
        });
        
        //or sync compress.
        BitmapResult result = Tiny.getInstance().source("").asBitmap().withOptions(options).compressSync();
```

#### **AsFile**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile, Throwable t) {
                //return the compressed file path
            }
        });
        
        //or sync compress.
        FileResult result = Tiny.getInstance().source("").asFile().withOptions(options).compressSync();
```
#### **AsFileWithReturnBitmap**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").asFile().withOptions(options).compress(new FileWithBitmapCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                //return the compressed file path and bitmap object
            }
        });
        
        //or sync compress.
        FileWithBitmapResult result = Tiny.getInstance().source("").asFile().withOptions(options).compressWithReturnBitmapSync();
```

#### **BatchAsBitmap**

```
        Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
        Tiny.getInstance().source("").batchAsBitmap().withOptions(options).batchCompress(new BitmapBatchCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap[] bitmaps, Throwable t) {
                //return the batch compressed bitmap object
            }
        });
        
        //or sync compress.
        BitmapBatchResult result = Tiny.getInstance().source("").batchAsBitmap().withOptions(options).batchCompressSync();
```
#### **BatchAsFile**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile, Throwable t) {
                //return the batch compressed file path
            }
        });
        
        //or sync compress.
        FileBatchResult result = Tiny.getInstance().source("").batchAsFile().withOptions(options).batchCompressSync();
```
#### **BatchAsFileWithReturnBitmap**

```
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source("").batchAsFile().withOptions(options).batchCompress(new FileWithBitmapBatchCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap[] bitmaps, String[] outfile, Throwable t) {
                //return the batch compressed file path and bitmap object
            }
        });
        
        //or sync compress.
        FileWithBitmapBatchResult result = Tiny.getInstance().source("").batchAsFile().withOptions(options).batchCompressWithReturnBitmapResult();
```

## **License**

>
>     Apache License
>
>     Version 2.0, January 2004
>     http://www.apache.org/licenses/
>
>     Copyright 2024 evannong
>
>  Licensed under the Apache License, Version 2.0 (the "License");
>  you may not use this file except in compliance with the License.
>  You may obtain a copy of the License at
>
>      http://www.apache.org/licenses/LICENSE-2.0
>
>  Unless required by applicable law or agreed to in writing, software
>  distributed under the License is distributed on an "AS IS" BASIS,
>  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>  See the License for the specific language governing permissions and
>  limitations under the License.

