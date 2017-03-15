set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [22061.63255955	20933.51254476	23090.002200850002	22996.43378748	22588.8535318];
y2 = [63935.16084051	57189.906617630004	64134.515941549995	63931.53679764	64358.88061789995];
y3 = [195.74515663	180.75456725	191.29914599	191.26767869	206.56410189];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the length of query set in Q');
ylabel('time (ms)');

axis([0.5 5.5 100.0 100000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'2','3','4','5','6'});
leg=legend('basic-g-v2','basic-w-v2','MDec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);