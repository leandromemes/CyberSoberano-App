# Créditos: dev Leandro - CyberSoberano
# Configuração de Proteção e Otimização de Código

# 1. Ativa a ofuscação (Removemos o -dontobfuscate)
# Isso transforma nomes de funções em letras como 'a', 'b', 'c' para proteger sua lógica.

# 2. Mantém as classes essenciais do Android e Firebase para não dar crash
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# 3. Protege as bibliotecas de interface (Glide e Material)
-keep public class com.github.bumptech.glide.** { *; }
-keep public class com.google.android.material.** { *; }

# 4. VITAL: Mantém as classes nativas do Terminal e JNI
# Se ofuscar isso, o motor do bot (npm start) para de funcionar!
-keep class com.cybersoberano.app.terminal.** { *; }
-keep class com.termux.** { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}

# 5. Remove logs de depuração para o APK de produção ficar mais limpo
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# 6. Configurações de Atributos de Segurança
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses
-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable