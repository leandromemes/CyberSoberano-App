import React from 'react';
import { StyleSheet, Text, View, ScrollView, TouchableOpacity, StatusBar } from 'react-native';

const App = () => {
  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#0a0a0a" />
      
      <View style={styles.header}>
        <Text style={styles.title}>🛡️ CyberSoberano</Text>
        <Text style={styles.subtitle}>Painel de Ferramentas Profissionais</Text>
      </View>

      <ScrollView style={styles.menu}>
        <TouchableOpacity style={styles.button} activeOpacity={0.7}>
          <Text style={styles.buttonText}>🔍 Escaneamento de Rede</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.button} activeOpacity={0.7}>
          <Text style={styles.buttonText}>🔑 Gerador de Hashes</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.button} activeOpacity={0.7}>
          <Text style={styles.buttonText}>🌐 Verificador de IP/DNS</Text>
        </TouchableOpacity>
        
        <TouchableOpacity style={[styles.button, {borderColor: '#ff0000'}]} activeOpacity={0.7}>
          <Text style={[styles.buttonText, {color: '#ff0000'}]}>⚠️ Teste de Vulnerabilidade</Text>
        </TouchableOpacity>
      </ScrollView>

      <View style={styles.footer}>
        <Text style={styles.credits}>Desenvolvido por Leandro</Text>
        <Text style={styles.credits}>Versão 1.0.0 - 2026</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#0a0a0a' },
  header: { 
    paddingTop: 60, 
    paddingBottom: 30, 
    alignItems: 'center', 
    borderBottomWidth: 1, 
    borderBottomColor: '#1a1a1a',
    backgroundColor: '#0f0f0f'
  },
  title: { color: '#00ff00', fontSize: 28, fontWeight: 'bold', letterSpacing: 2 },
  subtitle: { color: '#888', fontSize: 14, marginTop: 5 },
  menu: { padding: 20 },
  button: { 
    backgroundColor: '#111', 
    padding: 22, 
    borderRadius: 12, 
    marginBottom: 15, 
    borderWidth: 1, 
    borderColor: '#00ff00',
    elevation: 5 // Sombra no Android
  },
  buttonText: { color: '#fff', fontSize: 16, fontWeight: '600', textAlign: 'center' },
  footer: { padding: 20, alignItems: 'center' },
  credits: { color: '#333', fontSize: 12, fontWeight: 'bold' }
});

export default App;