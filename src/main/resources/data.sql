-- Customer 1: Jane Doe
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, tax_identification_number, domicile, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-7782XJ', 'Active', 'Individual_Brokerage', 'USR-441', 'Jane Doe', '***-**-1234', 'USA', 15420.50, 30841.00, 0.00, 'USD', false, 'Verified', 'Growth');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-990', 'ACC-7782XJ', 'Retirement_Growth_2045', '2020-05-15', 'Discretionary', 8, 'S&P 500', 'Quarterly', 125000.75, 15200.30, 1.15);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-101-AAPL', 'PORT-990', 'AAPL', 'Equity', 'NASDAQ', 'US0378331005', 50, 150.25, 7512.50, 185.40, 9270.00, 1757.50, 23.4, 0.074);


-- Customer 2: John Smith
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, tax_identification_number, domicile, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-1001-A', 'Active', 'Joint_Tenant', 'USR-102', 'John Smith', '***-**-5678', 'USA', 5000.00, 10000.00, 0.00, 'USD', false, 'Verified', 'Balanced');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-2001', 'ACC-1001-A', 'Education_Fund', '2019-01-10', 'Robo_Advisor', 6, 'Total Stock Market', 'Annually', 45000.00, 5000.00, 0.50);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-201-MSFT', 'PORT-2001', 'MSFT', 'Equity', 'NASDAQ', 'US5949181045', 20, 250.00, 5000.00, 350.00, 7000.00, 2000.00, 40.0, 0.155);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-202-GOOGL', 'PORT-2001', 'GOOGL', 'Equity', 'NASDAQ', 'US02079K3059', 15, 100.00, 1500.00, 140.00, 2100.00, 600.00, 40.0, 0.046);


-- Customer 3: Alice Johnson
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, tax_identification_number, domicile, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-3001-B', 'Active', 'Individual_Brokerage', 'USR-303', 'Alice Johnson', '***-**-9012', 'UK', 20000.00, 20000.00, 0.00, 'GBP', false, 'Verified', 'Aggressive_Growth');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-3001', 'ACC-3001-B', 'Tech_Heavy', '2021-06-01', 'Active_Management', 9, 'NASDAQ 100', 'Monthly', 80000.00, -2000.00, 2.5);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-301-TSLA', 'PORT-3001', 'TSLA', 'Equity', 'NASDAQ', 'US88160R1014', 100, 250.00, 25000.00, 210.00, 21000.00, -4000.00, -16.0, 0.2625);


-- Customer 4: Bob Brown
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, tax_identification_number, domicile, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-4001-C', 'Active', 'Margin', 'USR-404', 'Bob Brown', '***-**-3456', 'USA', 1000.00, 4000.00, 1500.00, 'USD', true, 'Verified', 'Speculative');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-4001', 'ACC-4001-C', 'Day_Trading_Alpha', '2023-01-01', 'Self_Directed', 10, 'None', 'Daily', 25000.00, 1200.00, -0.5);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-401-AMD', 'PORT-4001', 'AMD', 'Equity', 'NASDAQ', 'US0079031078', 200, 100.00, 20000.00, 115.00, 23000.00, 3000.00, 15.0, 0.92);


-- Customer 5: Charlie Davis
INSERT INTO accounts (account_id, status, type, owner_user_id, legal_name, tax_identification_number, domicile, settled_cash, buying_power, margin_balance, currency, is_pattern_day_trader, kyc_status, suitability_level)
VALUES ('ACC-5001-D', 'Inactive', 'IRA', 'USR-505', 'Charlie Davis', '***-**-7890', 'USA', 500.00, 500.00, 0.00, 'USD', false, 'Pending_Review', 'Conservative');

INSERT INTO portfolios (portfolio_id, linked_account_id, display_name, inception_date, management_style, target_risk_score, primary_benchmark, rebalancing_frequency, total_market_value, total_unrealized_pnl, daily_change_pct)
VALUES ('PORT-5001', 'ACC-5001-D', 'Safe_Harbor', '2015-11-20', 'Passive', 3, 'US Aggregate Bond', 'Annually', 10000.00, 200.00, 0.1);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-501-NVDA', 'PORT-5001', 'NVDA', 'Equity', 'NASDAQ', 'US67066G1040', 2, 400.00, 800.00, 500.00, 1000.00, 200.00, 25.0, 0.1);

INSERT INTO positions (position_id, portfolio_id, ticker, instrument_type, exchange, isin, quantity, average_buy_price, cost_basis, current_market_price, market_value, unrealized_gain_loss, unrealized_gain_loss_pct, weight_in_portfolio)
VALUES ('POS-502-INTC', 'PORT-5001', 'INTC', 'Equity', 'NASDAQ', 'US4581401001', 50, 40.00, 2000.00, 35.00, 1750.00, -250.00, -12.5, 0.175);
