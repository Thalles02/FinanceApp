# FinanceApp — Gerenciador Financeiro Pessoal

**Aplicativo Android**, com foco em boas práticas de interface (Material 3), arquitetura **MVVM** e persistência de dados local com **Room**.

## ✨ Visão Geral

O FinanceApp ajuda o usuário a registrar **receitas** e **despesas**, acompanhar o **saldo em tempo real** e visualizar o histórico filtrado por tipo e período.  
Todos os dados ficam gravados no dispositivo e permanecem disponíveis após fechar e reabrir o aplicativo.

## ✅ Funcionalidades Implementadas

| # | Funcionalidade | Descrição |
|---|----------------|-----------|
| 1 | **Cadastro** de transações | Formulário validado para inserir receitas ou despesas. |
| 2 | **Listagem** com filtro/busca | Filtros por tipo (receita/ despesa) e intervalo de datas, com pesquisa reativa. |
| 3 | **Edição** de transações | Diálogo pré-preenchido que permite alterar valores, datas e descrição. |
| 4 | **Exclusão** de transações | Swipe para deletar com *Snackbar* de “Desfazer”. |
| 5 | **Ordenação & Totais** | Ordenar por data ou valor e exibir totais de receitas, despesas e saldo. |

## 🗄️ Persistência de Dados

- **Room 2.6**  
- Entidade `Transaction`, DAO `TransactionDao`, conversores de tipo para `Date` e enum `TransactionType`.  
- Operações assíncronas com **Coroutines** e **Flow**, seguindo Repository Pattern.

## 🏗️ Arquitetura e Organização

com.example.financeapp/
data/ ← Entidades, DAOs, AppDatabase
repository/ ← TransactionRepository
ui/ ← Activities, adapters, dialogs
viewmodel/ ← TransactionViewModel

## 🛠️ Tecnologias & Bibliotecas

| Categoria                | Dependência / Versão |
|--------------------------|----------------------|
| Linguagem                | Kotlin |
| UI / Design              | AndroidX, Material 3 |
| Persistência             | Room + Coroutines + Flow |

## ▶️ Como Executar

1. **Clone** o repositório  
   ```bash
   git clone https://github.com/<seu-usuário>/FinanceApp.git
   cd FinanceApp
Abra no Android Studio Hedgehog (ou superior).

Conecte/emule um dispositivo API 24+ e clique em Run ▶️.

