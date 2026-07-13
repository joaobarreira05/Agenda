# Chefe - Assistente Pessoal Inteligente

**Chefe** (anteriormente "Agenda") é um assistente pessoal inteligente desenvolvido para Android, criado com o propósito de simplificar e agilizar o registo de tarefas e lembretes através de comandos de voz naturais. 

Esqueça a navegação por dezenas de menus e a digitação de datas; com o Chefe, basta falar o que precisa de fazer, quando e para quê, e o sistema trata de tudo automaticamente.

## 🚀 Funcionalidades Principais

- **Reconhecimento de Voz Natural:** Prima o botão de microfone e fale. O motor de reconhecimento converte a sua fala em texto em tempo real, guardando também o áudio original.
- **Extração Inteligente (NLP Local):** A app lê a sua frase e extrai automaticamente:
  - **A Data e Hora:** Suporta expressões naturais portuguesas como "amanhã", "às 14h30", "por volta das 11:30", "hoje".
  - **A Categoria:** Analisa a frase à procura de Categorias através de Fuzzy Matching (Distância de Levenshtein). Se mencionar o nome de uma categoria, a app categoriza a tarefa automaticamente.
  - **A Descrição:** A restante frase é filtrada (removendo expressões de tempo e categorias) para criar uma descrição de tarefa limpa e direta.
- **Confirmação Manual e Edição:** Após a análise, um ecrã de Confirmação é apresentado, permitindo a edição manual da hora, data, categoria e texto antes de guardar.
- **Alarmes Invasivos (Full-Screen Intent):** No momento em que um lembrete expira, a app acorda o telemóvel, tocando o Ringtone padrão por cima do ecrã de bloqueio, exigindo que marque a tarefa como vista ou a adie (Snooze de 10 min).
- **Gestão de Categorias Personalizada:** Crie, edite cores (através de códigos Hexadecimais) e elimine categorias diretamente nas definições. 
- **Conclusão em Lote e Alertas de Atraso:** As tarefas que ultrapassaram a data limite ficam assinaladas a vermelho e os cartões ganham um fundo de alerta. Selecione múltiplas tarefas e marque todas como "Visto" de uma só vez.

## 🛠 Tecnologias e Arquitetura

Este projeto foi construído respeitando as mais recentes diretrizes e boas práticas do ecossistema Android Moderno:

- **Linguagem:** Kotlin 100%
- **Arquitetura:** MVVM (Model-View-ViewModel) + Clean Architecture (Camadas: Presentation, Domain e Data)
- **Injeção de Dependências:** Dagger Hilt
- **Base de Dados:** Room Database (SQLite local)
- **Navegação:** Android Jetpack Navigation Component
- **Agendamento de Lembretes:** `AlarmManager` para garantir a execução exata dos alarmes e `BroadcastReceiver` + `Service` para mostrar as Notificações de Ecrã Inteiro (Full-Screen Intents).
- **Interface UI:** XML com componentes Material Design 3 e suporte completo a Edge-to-Edge (`fitsSystemWindows`).
- **Reconhecimento de Voz:** `SpeechRecognizer` nativo do Android.
- **Permissões em Tempo de Execução:** Suporte avançado às permissões de Notificações, Microfone e Alarmes Exatos (API 33 e API 34+).

## 📱 Como Usar

1. **Adicionar Tarefas:** No ecrã principal (Home), toque no botão flutuante de Microfone. Fale algo como: *"Lembrar de enviar email sobre avaliação de desempenho para a categoria ULS por volta das 11:30 amanhã"*.
2. **Rever e Guardar:** O ecrã de Confirmação irá aparecer. O sistema já selecionou a data (amanhã), a hora (11:30), a categoria (ULS) e limpou a frase. Verifique, ajuste se necessário (tocando nos relógios), e Guarde.
3. **Gestão de Categorias:** Toque na "roda dentada" (Definições) no canto superior direito para criar ou editar as cores e nomes das suas categorias.
4. **Alarme:** Quando chegar a hora, o ecrã liga-se. Tem as opções "Concluir" ou "Adiar (10 min)".
5. **Concluir Tarefas da Home:** Marque a *checkbox* à direita das tarefas pendentes. Pode selecionar várias. O botão inferior "Visto" aparece. Carregue nele para limpar a sua lista!

## ⚙️ Instalação (Para Desenvolvedores)

1. Clone este repositório.
2. Abra o projeto no **Android Studio**.
3. Sincronize o Gradle.
4. Conecte um emulador ou um dispositivo físico.
5. Execute a aplicação (Shift + F10) ou corra o comando no terminal: `./gradlew installDebug`.

## Licença
Este projeto é de caráter pessoal e fechado a um âmbito de assistente privado ("Chefe").
