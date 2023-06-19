const express = require("express");
const app = express();
const stripe = require("stripe")('sk_test_CGGvfNiIPwLXiDwaOfZ3oX6Y');

app.use(express.static("public"));
app.use(express.json());

const calculateOrderAmount = (items) => {
  return 500; // Reemplaza esto con el calculo del monto del pedido
};

app.get("/greet", async (req, res) => {
  res.send("Oh yeah, it's working!");
});

app.post("/create-payment-intent", async (req, res) => {
  const { items } = req.body;

  const paymentIntent = await stripe.paymentIntents.create({
    amount: calculateOrderAmount(items),
    currency: "usd",
    automatic_payment_methods: {
      enabled: true,
    },
  });

  res.send({
    clientSecret: paymentIntent.client_secret,
  });
});

app.listen(3000, () => console.log("Node server listening on port 3000!"));