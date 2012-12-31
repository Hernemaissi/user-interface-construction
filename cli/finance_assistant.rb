#!/usr/bin/env ruby
require 'launchy'

$account_balance = 0
$credit_balance = 0
$monthly_fees = 0
$monthly_pay = 0

def set_values()
  print "Enter Account Balance: "
  $account_balance = gets.chomp.to_f
  print "Enter Credit Balance: "
  $credit_balance = gets.chomp.to_f
  print "Enter monthly fees: "
  $monthly_fees = gets.chomp.to_f
  print "Enter monthly earned monthly: "
  $monthly_pay = gets.chomp.to_f
end

def calculate_invest()
  print "Please insert sum to invest: "
    invest = gets.chomp.to_f
    sum = invest
    print "After investment, your details will be:\n"
    sum = (sum - $account_balance > 0) ? sum - $account_balance : 0
    sum = (sum - $credit_balance > 0) ? sum - $credit_balance : 0
    loan_amount = sum
    if (loan_amount > 0)
      print "You need a loan of at least: #{loan_amount} euros\n"
      print "Use command 'loan' to get information about your banks loans\n"
      print "Or use command 'cloan' to open a loan calculator\n"
    end
    print "Suggested payment method: "
    new_account = 0
    new_credit = 0
    if ($account_balance >= invest)
      new_account = ($account_balance - invest > 0) ? $account_balance - invest : 0 
      new_credit = $credit_balance
      print "Pay from account"
    elsif ($credit_balance >= invest)
      new_account = $account_balance
      new_credit = ($credit_balance - invest > 0) ? $credit_balance - invest : 0
      print "Pay with credit card"
    elsif ($account_balance + $credit_balance > invest)
      new_account = ($account_balance - invest > 0) ? $account_balance - invest : 0
      left = invest - $account_balance
      new_credit = ($credit_balance - left > 0) ? $credit_balance - left : 0
      print "Pay with Account and credit"
    else
      new_account = $account_balance
      new_credit = $credit_balance
      print "Take a loan"
    end
    print "\n"
    print "Account Balance: #{new_account}\n"
    print "Credit card balance: #{new_credit}\n"
    difference = $monthly_fees - $monthly_pay
    if (difference > 0)
      months = (new_account.to_f / difference.to_f).ceil
      if (months <= 3)
        print "Warning! Your account funds will run out in #{months} months\n"
      end
    end
end

def get_commands(stub)
  commands = ["status", "set", "invest", "loan", "cloan", "help", "exit"]
  commands.reject! { |word| !word.include?(stub) }
  return commands
end

print "Welcome to Financial assistant\n"
print "Write 'status' to receive status of your current information\n"
print "Write 'set' to set the values to correct figures\n"
print "Write 'invest' to receive help in investing\n"
print "Finally, write 'help' for a full list of commands\n"
while(1)
  print "enter command:"
  command = gets.chomp
  if (command == "status" || command == "st")
    print "Account Balance: #{$account_balance}\n"
    print "Credit card balance: #{$credit_balance}\n"
    print "Monthly fees: #{$monthly_fees}\n"
    print "Money gained monthly: #{$monthly_pay}\n"
    difference = $monthly_fees - $monthly_pay
    if (difference > 0)
      months = ($account_balance.to_f / difference.to_f).ceil
      if (months <= 3)
        print "Warning! Your account funds will run out in #{months} months\n"
      end
    end
    print "Use command loan to get information about possible loans\n"
  elsif (command == "exit" || command == "e" )
    break
  elsif (command == "invest" || command == "i")
    calculate_invest()
    
  elsif (command == "set" || command == "s")
    set_values()
  elsif (command == "loan" || command == "l") 
    Launchy.open("http://www.nordea.fi/Henkil%C3%B6asiakkaat/Lainat/Lainat+ja+luotot/Joustoluotto+-+vakuudeton+kertaluotto/1105782.html?lnkID=product-box_joustoluotto_23-08-2012")
    print "This is the available loan information from your bank. If you wish to use a loan calculator, use command 'cloan'\n"
  elsif (command == "cloan" || command == "c")
    Launchy.open("http://www.nordea.fi/Henkil%C3%B6asiakkaat/Lainat/Lainat+ja+luotot/Joustoluotto-laskelma/42907.html")
  elsif (command == "help" || command == "h")
    print "Welcome to personal finance helper. Here is a list of available commands:\n"
    print "status - See all the values you have entered to the software\n"
    print "set - Set the values used by the software\n"
    print "invest - Enter a sum that you wish to invest and the program will suggest you how to finance it\n"
    print "loan - See your banks loan information\n"
    print "cloan - Access your banks loan calculator\n"
    print "help - Show this info\n"
    print "exit - quit the program\n"
  else
    print "Unkown command.\n"
    print "Use command 'help' for a list of commands\n"
    commands = get_commands(command[0])
    if (commands.size() > 0)
      print "Did you mean: \n"
      commands.each { |c| print "#{c}\n" }
    end
  end
end
print "Bye!\n"