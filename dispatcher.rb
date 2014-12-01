require 'bigbertha'
#gem install bigbertha before running

url = "https://intense-torch-3362.firebaseio.com"
ref = Bigbertha::Ref.new(url)
offerref = ref.child(:Offers)
requestsref = ref.child(:Requests)
running = true

while running
  requests = requestsref.val
  offers = offerref.val
  requests.each_pair do |k,v|
   if v["done"] != "Yes"
      offers.each_pair do |k2,v2|
        if v["Destination"] == v2["Destination"]
          requestsref.child(k,:MatchedOfferID).push(k2)
        end
      end
      requestsref.child(k).update(done:"Yes")
   else
      if requestsref.child(k).val["AskToJoin"] != nil
        requestsref.child(k,:AskToJoin).val.each_pair do |k3,v3|
          offerref.child(v3,:Requests).push(k)
          requestsref.child(k, :AskToJoin, k3).remove
        end
      end
   end
  end
  sleep 30
end
