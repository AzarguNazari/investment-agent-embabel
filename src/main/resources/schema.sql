CREATE TABLE accounts (
    account_id VARCHAR(50) PRIMARY KEY,
    status VARCHAR(50),
    type VARCHAR(50),
    owner_user_id VARCHAR(50),
    legal_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255),
    tax_identification_number VARCHAR(20),
    domicile VARCHAR(50),
    bank_name VARCHAR(100),
    iban VARCHAR(34),
    bic VARCHAR(11),
    settled_cash DOUBLE,
    buying_power DOUBLE,
    margin_balance DOUBLE,
    currency VARCHAR(10),
    is_pattern_day_trader BOOLEAN,
    kyc_status VARCHAR(50),
    suitability_level VARCHAR(50)
);

CREATE TABLE portfolios (
    portfolio_id VARCHAR(50) PRIMARY KEY,
    linked_account_id VARCHAR(50),
    display_name VARCHAR(100),
    inception_date VARCHAR(20),
    management_style VARCHAR(50),
    target_risk_score INT,
    primary_benchmark VARCHAR(50),
    rebalancing_frequency VARCHAR(50),
    total_market_value DOUBLE,
    total_unrealized_pnl DOUBLE,
    daily_change_pct DOUBLE
);

CREATE TABLE positions (
    position_id VARCHAR(50) PRIMARY KEY,
    portfolio_id VARCHAR(50),
    ticker VARCHAR(20),
    instrument_type VARCHAR(50),
    exchange VARCHAR(50),
    isin VARCHAR(50),
    quantity DOUBLE,
    average_buy_price DOUBLE,
    cost_basis DOUBLE,
    current_market_price DOUBLE,
    market_value DOUBLE,
    unrealized_gain_loss DOUBLE,
    unrealized_gain_loss_pct DOUBLE,
    weight_in_portfolio DOUBLE
);

CREATE TABLE transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    account_id VARCHAR(50),
    date VARCHAR(20),
    type VARCHAR(50),
    amount DOUBLE,
    description VARCHAR(255),
    status VARCHAR(50)
);

CREATE TABLE journals (
    journal_id VARCHAR(50) PRIMARY KEY,
    account_id VARCHAR(50),
    entry_date VARCHAR(20),
    debit_account VARCHAR(100),
    credit_account VARCHAR(100),
    amount DOUBLE,
    description VARCHAR(255)
);

CREATE TABLE documents (
    document_id VARCHAR(50) PRIMARY KEY,
    account_id VARCHAR(50),
    type VARCHAR(50),
    title VARCHAR(100),
    issue_date VARCHAR(20),
    url VARCHAR(255)
);
