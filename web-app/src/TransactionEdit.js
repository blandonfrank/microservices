import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

class TransactionEdit extends Component {

  emtpyTransaction = {
    id : '',
    userName : '',
    owner : '',
    amount : '',
    transactionType :'',
    date : '',
    commision : '',
    stock: {
        symbol: '',
        name : '',
        price: '',
        shares : ''
    }
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emtpyTransaction,
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async componentDidMount() {
    if (this.props.match.params.id !== 'new') {
      const transaction = await (await fetch(`/transactions/id/${this.props.match.params.id}`)).json();
      console.log(JSON.stringify(transaction));

      console.log(transaction.stock.symbol)
      this.setState({item: transaction});


    }
  }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }

  async handleSubmit(event) {
    event.preventDefault();
    const {item} = this.state;

    await fetch('/transactions', {
      method: (item.id) ? 'PUT' : 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item),
    });
    console.log(item)
    this.props.history.push('/transactions/' (item.id) ? 'update/' : 'create');
  }

  render() {
    const {item} = this.state;
    const title = <h2>{item.id ? 'Edit Transaction' : 'Add Transaction'}</h2>;

    return <div>
      <AppNavbar/>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
          <FormGroup>
            <Label for="name">Amount</Label>
            <Input type="text" name="amount" id="amount" value={item.amount || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="name">Transaction Type</Label>
            <Input type="text" name="transactionType" id="transactionType" value={item.transactionType || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="name">Commision</Label>
            <Input type="text" name="commision" id="commision" value={item.commision || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="name">Symbol</Label> 
            <Input type="text" name="stock.symbol" id="stock.symbol" value={item.stock.symbol || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="name">Price</Label>
            <Input type="text" name="city" id="city" value={item.stock.price || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="name">Date</Label>
            <Input type="text" name="date" id="date" value={item.date || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit">Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/transactions">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  }
}

export default withRouter(TransactionEdit);