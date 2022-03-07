class telegramManager extends Thread{

    String formatUrl;
    public telegramManager(String token) {
        formatUrl = "https://api.telegram.org/bot" + token;
    }

    @Override
    public void run() {
        while(true){





            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUpdates(){

    }
}
