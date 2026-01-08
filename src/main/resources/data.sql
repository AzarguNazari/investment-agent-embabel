-- Customer 1: Jane Doe
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, email, phone, address, tax_identification_number, domicile, bank_name, iban, bic, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-7782XJ', 'Active', 'Individual_Brokerage', 'USR-441', 'Jane Doe', 'jane.doe@example.com', '+1-555-0101', '123 Wall St, New York, NY', '***-**-1234', 'USA', 'Global Bank Corp', 'US89 GBC 0000 1234 5678 90', 'GBCRUS33', 15420.50, 30841.00, 0.00, 'USD', false, 'Verified', 'Growth');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-990', 'ACC-7782XJ', 'Retirement_Growth_2045', '2020-05-15', 'Discretionary', 8, 'S&P 500', 'Quarterly', 125000.75, 15200.30, 1.15);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-101-AAPL', 'PORT-990', 'AAPL', 'Equity', 'NASDAQ', 'US0378331005', 50, 150.25, 7512.50, 185.40, 9270.00, 1757.50, 23.4, 0.074);


-- Customer 2: John Smith
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, email, phone, address, tax_identification_number, domicile, bank_name, iban, bic, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-1001-A', 'Active', 'Joint_Tenant', 'USR-102', 'John Smith', 'john.smith@gmail.com', '+1-555-0202', '456 Oak Ave, San Francisco, CA', '***-**-5678', 'USA', 'Pacific Union Bank', 'US77 PUB 1111 2222 3333 44', 'PUBUS66', 5000.00, 10000.00, 0.00, 'USD', false, 'Verified', 'Balanced');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-2001', 'ACC-1001-A', 'Education_Fund', '2019-01-10', 'Robo_Advisor', 6, 'Total Stock Market', 'Annually', 45000.00, 5000.00, 0.50);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-201-MSFT', 'PORT-2001', 'MSFT', 'Equity', 'NASDAQ', 'US5949181045', 20, 250.00, 5000.00, 350.00, 7000.00, 2000.00, 40.0, 0.155);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-202-GOOGL', 'PORT-2001', 'GOOGL', 'Equity', 'NASDAQ', 'US02079K3059', 15, 100.00, 1500.00, 140.00, 2100.00, 600.00, 40.0, 0.046);


-- Customer 3: Alice Johnson
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, email, phone, address, tax_identification_number, domicile, bank_name, iban, bic, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-3001-B', 'Active', 'Individual_Brokerage', 'USR-303', 'Alice Johnson', 'alice.j@outlook.co.uk', '+44 20 7946 0958', '10 Downing St, London', '***-**-9012', 'UK', 'Royal Bank', 'UK12 RYB 0000 5555 6666 77', 'RYBGB22', 20000.00, 20000.00, 0.00, 'GBP', false, 'Verified', 'Aggressive_Growth');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-3001', 'ACC-3001-B', 'Tech_Heavy', '2021-06-01', 'Active_Management', 9, 'NASDAQ 100', 'Monthly', 80000.00, -2000.00, 2.5);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-301-TSLA', 'PORT-3001', 'TSLA', 'Equity', 'NASDAQ', 'US88160R1014', 100, 250.00, 25000.00, 210.00, 21000.00, -4000.00, -16.0, 0.2625);


-- Transactions Data
INSERT INTO transactions (transaction_id, account_id, date, type, amount, description, status) VALUES
('TX-001', 'ACC-7782XJ', '2023-12-01', 'DEPOSIT', 5000.0, 'Initial deposit via Wire', 'COMPLETED'),
('TX-002', 'ACC-7782XJ', '2023-12-05', 'BUY', -2000.0, 'Bought 10 AAPL', 'COMPLETED'),
('TX-003', 'ACC-1001-A', '2023-11-20', 'DIVIDEND', 45.50, 'Dividend MSFT', 'COMPLETED'),
('TX-004', 'ACC-3001-B', '2023-12-10', 'WITHDRAWAL', -100.0, 'ATM Withdrawal', 'COMPLETED');

-- Journals Data
INSERT INTO journals (journal_id, account_id, entry_date, debit_account, credit_account, amount, description) VALUES
('JRN-001', 'ACC-7782XJ', '2023-12-01', 'Cash at Bank', 'Customer Equity', 5000.0, 'Funding of account'),
('JRN-002', 'ACC-7782XJ', '2023-12-05', 'Equities - Tech', 'Cash at Bank', 2000.0, 'Stock Purchase AAPL');

-- Documents Data
INSERT INTO documents (document_id, account_id, type, title, issue_date, url) VALUES
('DOC-001', 'ACC-7782XJ', 'STATEMENT', 'Monthly Statement - Nov 2023', '2023-12-01', 'https://example.com/docs/acc7782/stmt-nov-2023.pdf'),
('DOC-002', 'ACC-7782XJ', 'TAX', 'Form 1099-DIV 2023', '2024-01-15', 'https://example.com/docs/acc7782/tax-2023.pdf'),
('DOC-003', 'ACC-1001-A', 'ADVISORY', 'Quarterly Review Report', '2023-10-05', 'https://example.com/docs/acc1001/q3-review.pdf');
