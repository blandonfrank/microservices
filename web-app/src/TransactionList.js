import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class TransactionList extends Component {

  constructor(props) {
    super(props);
    this.state = {transactions: [], isLoading: true};
    this.remove = this.remove.bind(this);
  }

  componentDidMount() {
    this.setState({isLoading: true});

    fetch('transactions/admin/all')
      .then(response => response.json())
      .then(data => this.setState({transactions: data, isLoading: false}));
  }

  async remove(id) {
    await fetch(`/transactions/delete/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      let updatedTransactions = [...this.state.transactions].filter(i => i.id !== id);
      this.setState({transactions: updatedTransactions});
    });
  }

  render() {
    const {transactions, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const transactionList = transactions.map(transaction => {
      return <tr key={transaction.id}>
        <td style={{whiteSpace: 'nowrap'}}>{new Intl.DateTimeFormat('en-US', {
            year: 'numeric',
            month: 'long',
            day: '2-digit'
          }).format(new Date(transaction.date))}</td>
        <td>{transaction.amount}</td>
        <td>{transaction.shares}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/transactions/update" + transaction.id}>Edit</Button>
            <Button size="sm" color="danger" onClick={() => this.remove(transaction.id)}>Delete</Button>
          </ButtonGroup>
        </td>
      </tr>
    });

    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/transactions/create">Add Transaction</Button>
          </div>
          <h3>My Transactions</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Date</th>
              <th width="20%">Amount</th>
              <th>Companies</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {transactionList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}

export default TransactionList;